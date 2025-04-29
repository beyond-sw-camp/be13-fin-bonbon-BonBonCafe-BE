package com.beyond.Team3.bonbon.user.service;

import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.entity.Franchisee;
import com.beyond.Team3.bonbon.franchise.entity.Manager;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.handler.exception.FranchiseException;
import com.beyond.Team3.bonbon.user.repository.FranchiseeRepository;
import com.beyond.Team3.bonbon.user.repository.ManagerRepository;
import com.beyond.Team3.bonbon.handler.exception.PageException;
import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import com.beyond.Team3.bonbon.user.dto.FranchiseeRegisterDto;
import com.beyond.Team3.bonbon.user.dto.ManagerRegisterDto;
import com.beyond.Team3.bonbon.user.dto.PasswordModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserInfoDto;
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

    // 계정 생성 -> only HEADQUARTER만
    @Transactional
    public void joinManager(ManagerRegisterDto managerRegisterDto, Principal principal) {

        User registUser = join(managerRegisterDto, principal, Role.MANAGER);

        // manager 테이블에 담당자 추가
        Manager manager = Manager.builder()
                .userId(registUser)
                .headquarterId(registUser.getHeadquarterId())
                .region(managerRegisterDto.getRegion())
                .build();

        managerRepository.save(manager);
    }

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

        String headquarterEmail = principal.getName();
        User headquarter = userRepository.findByEmail(headquarterEmail)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

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
    @Transactional
    public UserInfoDto getUser(Principal principal) {

        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        UserInfoDto userInfoDto = new UserInfoDto(user);

        return userInfoDto;
    }

    // 비밀번호 변경
    @Transactional
    public void modifyPassword(Principal principal, PasswordModifyDto passwordModifyDto) {

        String userEmail = principal.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

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
    public void update(Principal principal, UserModifyDto userModifyDto) {
        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
        user.userInfoUpdate(userModifyDto);
    }

    @Override
    public Page<UserInfoDto> getAccounts(int page, int size, Principal principal) {

        String userEmail = principal.getName();
        User headquarter = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);
        if(page < 0 || size <= 0){
            throw new PageException(ExceptionMessage.INVALID_PAGE_PARAMETER);
        }

        Page<User> users = userRepository.findByParentId(headquarter, pageable);
        Page<UserInfoDto> result =  users.map(UserInfoDto::new);
        return result;
    }


    // 가입하려는 email이 이미 존재하는 이메일인지 확인
    public void checkEmailDuplication(String email){

        Optional<User> findUsers = userRepository.findByEmail(email);
        if(findUsers.isPresent()){
            throw new UserException(ExceptionMessage.EMAIL_ALREADY_EXIST);
        }
    }


}
