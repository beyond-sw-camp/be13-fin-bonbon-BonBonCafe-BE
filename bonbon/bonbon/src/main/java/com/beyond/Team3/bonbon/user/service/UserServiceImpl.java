package com.beyond.Team3.bonbon.user.service;

import com.beyond.Team3.bonbon.common.enums.AccountStatus;
import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.entity.Franchisee;
import com.beyond.Team3.bonbon.franchise.entity.Manager;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.handler.exception.FranchiseException;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquarter.repository.HeadquarterRepository;
import com.beyond.Team3.bonbon.region.entity.Region;
import com.beyond.Team3.bonbon.region.repository.RegionRepository;
import com.beyond.Team3.bonbon.user.dto.FranchiseFilterDto;
import com.beyond.Team3.bonbon.user.dto.FranchiseeInfoDto;
import com.beyond.Team3.bonbon.user.dto.FranchiseeModifyDto;
import com.beyond.Team3.bonbon.user.dto.ManagerInfoDto;
import com.beyond.Team3.bonbon.user.dto.ManagerModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserInfoDto;
import com.beyond.Team3.bonbon.user.repository.FranchiseeRepository;
import com.beyond.Team3.bonbon.user.repository.ManagerRepository;
import com.beyond.Team3.bonbon.handler.exception.PageException;
import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import com.beyond.Team3.bonbon.user.dto.FranchiseeRegisterDto;
import com.beyond.Team3.bonbon.user.dto.ManagerRegisterDto;
import com.beyond.Team3.bonbon.user.dto.PasswordModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserRegisterDto;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
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
    private final HeadquarterRepository headquarterRepository;

    // Manger 계정 생성
    @Override
    @Transactional
    public void joinManager(ManagerRegisterDto managerRegisterDto) {

        User registUser = join(managerRegisterDto, managerRegisterDto.getHeadquarterId(), Role.MANAGER);

        if(managerRegisterDto.getRegionCode() != 0){
            Region byRegionCode = regionRepository.findByRegionCode(managerRegisterDto.getRegionCode());

            // manager 테이블에 담당자 추가
            Manager manager = Manager.builder()
                    .userId(registUser)
                    .regionCode(byRegionCode)
                    .build();
            managerRepository.save(manager);
        } else {
            // manager 테이블에 담당자 추가
            Manager manager = Manager.builder()
                    .userId(registUser)
                    .regionCode(null)
                    .build();
            managerRepository.save(manager);
        }

    }

    // 가맹점주 등록
    @Override
    @Transactional
    public void joinFranchisee(FranchiseeRegisterDto franchiseeRegisterDto) {

        User registUser = join(franchiseeRegisterDto, franchiseeRegisterDto.getHeadquarterId(), Role.FRANCHISEE);

        // 가맹점 등록까지 같이 하는 경우
        if(franchiseeRegisterDto.getFranchiseId() != null){

            // 가맹점 ID 유효성 확인
            Franchise franchise = franchiseRepository.findById(franchiseeRegisterDto.getFranchiseId())
                    .orElseThrow(() -> new FranchiseException(ExceptionMessage.FRANCHISE_NOT_FOUND));

            Franchisee franchisee = Franchisee.builder()
                    .userId(registUser)
                    .franchise(franchise)
                    .build();
            franchiseeRepository.save(franchisee);

        } else {
            // 가맹점 등록 없이 가맹점주만 등록하는 경우
            Franchisee franchisee = Franchisee.builder()
                    .userId(registUser)
                    .franchise(null)
                    .build();
            franchiseeRepository.save(franchisee);
        }
    }

    // 현재 로그인한 사용자 본인 정보 조회
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

    // 사용자 본인 정보 업데이트
    @Override
    @Transactional
    public void update(Principal principal, UserModifyDto userModifyDto) {
        User user = getCurrentUser(principal);
        user.userInfoUpdate(userModifyDto);
    }

    // 본사에서 등록 사용자 정보 수정
    @Override
    @Transactional
    public void registUserUpdate(Long userId, UserModifyDto userModifyDto, Principal principal) {

        User user = checkAuthorization(userId, principal);

        user.userInfoUpdate(userModifyDto);
    }

    // 가맹점주 정보 업데이트
    @Override
    @Transactional
    public void franchiseeUpdate(Long franchiseeId, FranchiseeModifyDto franchiseeModifyDto, Principal principal) {
        // headquarter 확인
        User user = getCurrentUser(principal);

        // franchiseeId로 가맹점주 정보 찾기 -> 없으면 에러
        Franchisee franchisee = franchiseeRepository.findById(franchiseeId)
                .orElseThrow(() -> new FranchiseException(ExceptionMessage.USER_NOT_FOUND));

        franchisee.getUserId().userInfoUpdate(franchiseeModifyDto);

        // Modify에서 입력한 가맹점을 찾고 -> 없으면 그냥 null
        if(franchiseeModifyDto.getFranchiseId() != null ){
            Franchise franchise = franchiseRepository.findById(franchiseeModifyDto.getFranchiseId())
                    .orElseThrow(() -> new FranchiseException(ExceptionMessage.FRANCHISE_NOT_FOUND));
            franchisee.setFranchise(franchise);
        } else {
            franchisee.setFranchise(null);
        }
    }

    @Override
    @Transactional
    public void managerUpdate(Long managerId, ManagerModifyDto managerModifyDto, Principal principal) {
        // headquarter 확인
        User user = getCurrentUser(principal);

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        manager.getUserId().userInfoUpdate(managerModifyDto);

        // Modify에서 입력한 지역 코드를 찾고 -> 없으면 그냥 null
        if(managerModifyDto.getRegionCode() != null ){
            Region region = regionRepository.findById(managerModifyDto.getRegionCode())
                    .orElseThrow(() -> new UserException(ExceptionMessage.FRANCHISE_NOT_FOUND));
            manager.setRegionCode(region);
        } else {
            manager.setRegionCode(null);
        }

    }

    @Override
    public Page<FranchiseFilterDto> findFranchiseWithoutOwnerPrincipal(int page, int size, Principal principal) {
        // 본사 정보 가져오기
        User currentUser = getCurrentUser(principal);

        Pageable pageable = PageRequest.of(page, size);
        if(page < 0 || size <= 0){
            throw new PageException(ExceptionMessage.INVALID_PAGE_PARAMETER);
        }

        Page<Franchise> withoutOwner = franchiseRepository.findWithoutOwner(currentUser.getHeadquarterId(), pageable);

        return withoutOwner.map(FranchiseFilterDto::new);
    }

    // 생성한 Franchisee 계정 전체 조회
    @Override
    @Transactional(readOnly = true)
    public Page<FranchiseeInfoDto> getFranchiseeAccounts(int page, int size, Role role, Principal principal) {

        // 본사 정보 가져오기
        User parentId = getCurrentUser(principal);

        // 매니저인지 확인
        if(!parentId.getUserType().equals(Role.HEADQUARTER)){
            throw new UserException(ExceptionMessage.INVALID_USER_ROLE);
        }

        Pageable pageable = PageRequest.of(page, size);
        if(page < 0 || size <= 0){
            throw new PageException(ExceptionMessage.INVALID_PAGE_PARAMETER);
        }

        Page<Franchisee> franchiseesFromHeadquarter = franchiseeRepository.findFranchiseesFromHeadquarter(parentId, pageable);

        // role 입력 -> 조회
//        Page<User> users = userRepository.findByParentIdAndUserType(parentId, role, pageable);
        return franchiseesFromHeadquarter.map(FranchiseeInfoDto::new);
    }

    @Override
    @Transactional
    public Page<ManagerInfoDto> getManagerAccounts(int page, int size, Principal principal) {

        // 본사 정보 가져오기
        User parentId = getCurrentUser(principal);

        Pageable pageable = PageRequest.of(page, size);
        if(page < 0 || size <= 0){
            throw new PageException(ExceptionMessage.INVALID_PAGE_PARAMETER);
        }

        Page<Manager> manager = managerRepository.findManagerFromHeadquarter(parentId, pageable);

        return manager.map(ManagerInfoDto::new);
    }



    // 특정 매니저 계정 정보 확인
    @Override
    @Transactional
    public ManagerInfoDto getManagerDetail(Long userId, Principal principal) {
        // 본사 확인 -> 접근 권한 있는지 확인
        User user = checkAuthorization(userId, principal);

        // 매니저인지 확인
        if(!user.getUserType().equals(Role.MANAGER)){
            throw new UserException(ExceptionMessage.INVALID_USER_ROLE);
        }

        // manager 테이블에서 먼저 찾고
        Manager manager = managerRepository.findByUserId(user)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        // MangerInfoDto로 변환
        ManagerInfoDto managerInfoDto = new ManagerInfoDto(manager);

        if(manager.getRegionCode() != null){
            managerInfoDto.setRegion(manager.getRegionCode().getRegionName());
            managerInfoDto.setRegionCode(manager.getRegionCode().getRegionCode());
        } else {
            managerInfoDto.setRegion(null);
        }
        return managerInfoDto;
    }

    // 특정 가맹점주 정보 조회
    @Override
    @Transactional
    public FranchiseeInfoDto getFranchiseeDetail(Long userId, Principal principal) {
        // 본사 확인 -> 접근 권한 있는지 확인, userId로 사용자 찾기
        User user = checkAuthorization(userId, principal);

        // 가맹점주인지 확인
        if(!user.getUserType().equals(Role.FRANCHISEE)){
            throw new UserException(ExceptionMessage.INVALID_USER_ROLE);
        }

        Franchisee franchisee = franchiseeRepository.findByUserId(user)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        FranchiseeInfoDto franchiseeInfoDto = new FranchiseeInfoDto(user);
        franchiseeInfoDto.setFranchiseeId(franchisee.getFranchiseeId());

        if(franchisee.getFranchise() != null){
            franchiseeInfoDto.setFranchiseId(franchisee.getFranchise().getFranchiseId());
            franchiseeInfoDto.setFranchiseName(franchisee.getFranchise().getName());
        } else {
            franchiseeInfoDto.setFranchiseId(null); // 연결된 가맹점이 없으면 null로 일단 띄우기
            franchiseeInfoDto.setFranchiseName("가맹점 없음");
        }

        return franchiseeInfoDto;
    }

    // 가맹점주 계정 삭제
    @Override
    @Transactional
    public void deleteFranchisee(Long userId, Principal principal) {

        // 지우려는 사용자 확인
        User deleteUser = checkAuthorization(userId, principal);

        Franchisee byUserId = franchiseeRepository.findByUserId(deleteUser)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        // 연관관계 사전 삭제
        byUserId.getFranchise().disconnectFranchisee();

        // Franchisee 테이블에서 데이터 먼저 삭제 후 Account 삭제
        franchiseeRepository.deleteByFranchiseeId(byUserId.getFranchiseeId());

        // 사용자 계정 삭제 스케줄링
        deleteUser(deleteUser);
    }

    // 담당자 계정 삭제
    @Override
    @Transactional
    public void deleteManager(Long userId, Principal principal) {
        // 지우려는 사용자 확인
        User deleteUser = checkAuthorization(userId, principal);

        managerRepository.deleteByUserId(deleteUser);

        deleteUser(deleteUser);
    }

    @Override
    @Transactional
    public Page<FranchiseFilterDto> findFranchiseWithoutOwner(int page, int size, Long headquarterId) {
        // 본사 정보 가져오기

        Headquarter headquarter = headquarterRepository.findById(headquarterId)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);
        if(page < 0 || size <= 0){
            throw new PageException(ExceptionMessage.INVALID_PAGE_PARAMETER);
        }

        Page<Franchise> withoutOwner = franchiseRepository.findWithoutOwner(headquarter, pageable);

        return withoutOwner.map(FranchiseFilterDto::new);
    }

    // 특정 지역에 있는 Frachise들 전부 가져오기
    @Override
    public Page<FranchiseFilterDto> getFranchiseInRegion(int regionCode, int page, int size, Principal principal) {
        User headquarter = getCurrentUser(principal);

        Pageable pageable = PageRequest.of(page, size);
        if(page < 0 || size <= 0){
            throw new PageException(ExceptionMessage.INVALID_PAGE_PARAMETER);
        }

        Page<Franchise> franchiseListInRegion = franchiseRepository.findFranchiseListInRegion(regionCode, headquarter.getHeadquarterId(), pageable);

        return franchiseListInRegion.map(FranchiseFilterDto::new );
    }

    // 사용자 계정 삭제
    public void deleteUser(User user) {

        // 계정 상태 -> DELETED로 변경
        user.setStatus(AccountStatus.DELETED);
        // 삭제 예정일을 3일 뒤로 설정
        user.setDeletedAt(LocalDateTime.now().plusDays(3));
    }

    // 사용자 정보 조회
    @Override
    @Transactional
    public UserInfoDto getAccountDetail(Long userId, Principal principal) {

        User user = checkAuthorization(userId, principal);
        return new UserInfoDto(user);
    }

    // 사용자가 headquarter인지 확인 ->
    private User checkAuthorization(Long userId, Principal principal) {

        User headquarter = getCurrentUser(principal);

        // 찾은 사용자 role이 Headquarter가 아니면 에러 던지기
        if(!headquarter.getUserType().equals(Role.HEADQUARTER)){
            throw new UserException(ExceptionMessage.UNAUTHORIZED_ACCOUNT_ACCESS);
        }

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

    // 사용자 계정 등록 -> 이건 본사 확인 딱히 필요 없지 않음?
    public User join(UserRegisterDto userRegisterDto, Long headquarterId, Role role) {

        // 본사 확인
        Headquarter headquarter = headquarterRepository.findById(headquarterId)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        // 본사 계정 확인
        User heaquarterAccount = userRepository.findByUserTypeAndHeadquarterId(Role.HEADQUARTER, headquarter)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        // 등록하려는 email 중복 확인
        checkEmailDuplication(userRegisterDto.getEmail());

//        // 비밀번호, 비밀번호 확인 일치
//        if (!userRegisterDto.passwordMatching()) {
//            throw new UserException(ExceptionMessage.PASSWORD_NOT_MATCH);
//        }

        // 사용자 생성, 비밀번호 encoding
        User user = userRegisterDto.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 본사 계정을 부모 ID로 추가
        user.setParentId(heaquarterAccount);
        // 본사 ID 추가
        user.setHeadquarterId(headquarter);
        user.setUserType(role);
        userRepository.save(user);

        return user;
    }
}
