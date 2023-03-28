package me.ctrlmaniac.api.repositories.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.ctrlmaniac.api.entities.account.AccountRole;
import me.ctrlmaniac.api.enums.AccountRoleEnum;

public interface AccountRoleRepo extends JpaRepository<AccountRole, String> {
	Optional<AccountRole> findByAuthority(AccountRoleEnum role);
}