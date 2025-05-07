package com.beyond.Team3.bonbon.user.service;

import com.beyond.Team3.bonbon.common.enums.AccountStatus;
import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.entity.Franchisee;
import com.beyond.Team3.bonbon.franchise.entity.Manager;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.handler.exception.FranchiseException;
import com.beyond.Team3.bonbon.region.repository.RegionRepository;
import com.beyond.Team3.bonbon.user.dto.ManagerInfoDto;
import com.beyond.Team3.bonbon.user.dto.UserInfoDto;
import com.beyond.Team3.bonbon.user.repository.FranchiseeRepository;
import com.beyond.Team3.bonbon.user.repository.ManagerRepository;
import com.beyond.Team3.bonbon.handler.exception.PageException;
import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import com.beyond.Team3.bonbon.user.dto.FranchiseeRegisterDto;
import com.beyond.Team3.bonbon.user.dto.ManagerRegisterDto;
import com.beyond.Team3.bonbon.user.dto.PasswordModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserInfo;
import com.beyond.Team3.bonbon.user.dto.UserModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserRegisterDto;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final FranchiseeRepository franchiseeRepository;
    private final FranchiseRepository franchiseRepository;
    private final RegionRepository regionRepository;

    // Manger 계정 생성
    @Override
    @Transactional
    public void joinManager(ManagerRegisterDto managerRegisterDto, Principal principal) {

        User registUser = join(managerRegisterDto, principal, Role.MANAGER);

        // manager 테이블에 담당자 추가
        Manager manager = Manager.builder()
                .userId(registUser)
                .regionCode(managerRegisterDto.getRegionCode())
                .build();

        managerRepository.save(manager);
    }

    @Override
    @Transactional
    public void joinFranchisee(FranchiseeRegisterDto franchiseeRegisterDto, Principal principal) {

        User registUser = join(franchiseeRegisterDto, principal, Role.FRANCHISEE);

        Franchise franchise = franchiseRepository.findById(franchiseeRegisterDto.getFranchiseId())
                .orElseThrow(() -> new FranchiseException(ExceptionMessage.FRANCHISE_NOT_FOUND));

        Franchisee franchisee = Franchisee.builder()
                .userId(registUser)
                .franchise(franchise)
                .build();

        franchiseeRepository.save(franchisee);
    }

    public User join(UserRegisterDto userRegisterDto, Principal principal, Role role) {

        User headquarter = getCurrentUser(principal);

        // email 중복 확인
        checkEmailDuplication(userRegisterDto.getEmail());

        // 비밀번호, 비밀번호 확인 일치
        if (!userRegisterDto.passwordMatching()) {
            throw new UserException(ExceptionMessage.PASSWORD_NOT_MATCH);
        }

        // 사용자 생성, 비밀번호 encoding
        User user = userRegisterDto.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 계정 생성자 ID
        user.setParentId(headquarter);
        // 본사 ID
        user.setHeadquarterId(headquarter.getHeadquarterId());
        user.setUserType(role);
        userRepository.save(user);

        return user;
    }

    // 사용자 정보 조회
    @Override
    @Transactional
    public UserInfoDto getUser(Principal principal) {

        User user = getCurrentUser(principal);
        return new UserInfoDto(user);
    }

    // 비밀번호 변경
    @Override
    @Transactional
    public void modifyPassword(Principal principal, PasswordModifyDto passwordModifyDto) {

        User user = getCurrentUser(principal);

        // 이전 비밀번호 일치 / 새로운 비밀번호, 비밀번호 확인이 일치하는지 확인
        if(!passwordEncoder.matches(passwordModifyDto.getOldPassword(), user.getPassword())
                || !passwordModifyDto.getNewPassword().equals(passwordModifyDto.getNewPasswordConfirm()))
        {
            throw new UserException(ExceptionMessage.PASSWORD_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(passwordModifyDto.getNewPassword()));
    }

    // 사용자 정보 업데이트
    @Override
    @Transactional
    public void update(Principal principal, UserModifyDto userModifyDto) {
        User user = getCurrentUser(principal);
        user.userInfoUpdate(userModifyDto);
    }

    @Override
    @Transactional
    public void registUserUpdate(Long userId, UserModifyDto userModifyDto, Principal principal) {

        User user = checkAuthorization(userId, principal);

        user.userInfoUpdate(userModifyDto);
    }

    // 생성한 계정 전체 조회 -> 이후에 Manager 별, Franchise 별, 이름 별 검색 가능하도록
    @Override
    @Transactional
    public Page<UserInfoDto> getAccountsByRole(int page, int size, Role role, Principal principal) {

        // 본사 정보 가져오기
        User parent = getCurrentUser(principal);

        Pageable pageable = PageRequest.of(page, size);
        if(page < 0 || size <= 0){
            throw new PageException(ExceptionMessage.INVALID_PAGE_PARAMETER);
        }

        // role 입력 -> 조회
        Page<User> users = userRepository.findByParentIdAndUserType(parent, role, pageable);
        return users.map(UserInfoDto::new);
    }

    @Override
    @Transactional
    public ManagerInfoDto getManagerDetail(Long userId, Principal principal) {
        // 본사 확인 -> 접근 권한 있는지 확인
        User user = checkAuthorization(userId, principal);

        // 매니저인지 확인
        if(!user.getUserType().equals(Role.MANAGER)){
            throw new UserException(ExceptionMessage.INVALID_USER_ROLE);
        }

        // MangerInfoDto로 변환
        ManagerInfoDto managerInfoDto = new ManagerInfoDto(user);

        // manager 테이블에서 먼저 찾고
        Manager manager = managerRepository.findByUserId(user)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        managerInfoDto.setRegion(manager.getRegionCode().getRegionName());

        return managerInfoDto;
    }

    @Override
    @Transactional
    public ManagerInfoDto getFranchiseeDetail(Long userId, Principal principal) {
        // 본사 확인 -> 접근 권한 있는지 확인
        User user = checkAuthorization(userId, principal);

        // 매니저인지 확인
        if(!user.getUserType().equals(Role.FRANCHISEE)){
            throw new UserException(ExceptionMessage.INVALID_USER_ROLE);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId, Principal principal) {

        // 지우려는 사용자 확인
        User user = checkAuthorization(userId, principal);

        // 계정 상태 -> DELETED로 변경
        user.setStatus(AccountStatus.DELETED);
        // 삭제 예정일을 3일 뒤로 설정
        user.setDeletedAt(LocalDateTime.now().plusDays(3));
    }

    @Override
    @Transactional
    public UserInfoDto getAccountDetail(Long userId, Principal principal) {

        User user = checkAuthorization(userId, principal);
        return new UserInfoDto(user);
    }

    // 사용자가 headquarter인지 확인
    private User checkAuthorization(Long userId, Principal principal) {

        User headquarter = getCurrentUser(principal);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        // 사용자에 대한 권한이 일치하는지 확인
        if(!user.getHeadquarterId().equals(headquarter.getHeadquarterId())){
            throw new UserException(ExceptionMessage.UNAUTHORIZED_ACCOUNT_ACCESS);
        }
        return user;
    }

    // 가입하려는 email이 이미 존재하는 이메일인지 확인
    public void checkEmailDuplication(String email){

        Optional<User> findUsers = userRepository.findByEmail(email);
        if(findUsers.isPresent()){
            throw new UserException(ExceptionMessage.EMAIL_ALREADY_EXIST);
        }
    }

    // 현재 사용자 확인
    public User getCurrentUser(Principal principal) {
        String userEmail = principal.getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }
}
