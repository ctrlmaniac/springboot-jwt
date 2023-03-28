package me.ctrlmaniac.api;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import me.ctrlmaniac.api.entities.account.Account;
import me.ctrlmaniac.api.entities.account.AccountRole;
import me.ctrlmaniac.api.enums.AccountRoleEnum;
import me.ctrlmaniac.api.services.account.AccountRoleService;
import me.ctrlmaniac.api.services.account.AccountService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SetUpRunner implements CommandLineRunner {

	@Value("${admin.email}")
	private String adminEmail;

	@Value("${admin.fname}")
	private String adminFName;

	@Value("${admin.lname}")
	private String adminLName;

	@Value("${admin.password}")
	private String adminPassword;

	@Autowired
	private AccountRoleService accountRoleService;

	@Autowired
	private AccountService accountService;

	@Override
	public void run(String... args) throws Exception {
		log.info("Running setup...");

		log.info("Adding account roles");
		// Save roles
		for (AccountRoleEnum role : AccountRoleEnum.values()) {
			AccountRole savedRole = accountRoleService.findByName(role);

			if (savedRole == null) {
				accountRoleService.save(new AccountRole(role));
			}
		}

		log.info("adding admin");
		// Create admin
		if (!accountService.existsByEmail(adminEmail)) {
			AccountRole roleAdmin = accountRoleService.findByName(AccountRoleEnum.ADMIN);
			Set<AccountRole> adminAuthorities = new HashSet<>();
			adminAuthorities.add(roleAdmin);

			Account admin = new Account(adminEmail, adminFName, adminLName,
					adminPassword, true, true, true, true,
					adminAuthorities);
			accountService.create(admin);
		}
	}

}
