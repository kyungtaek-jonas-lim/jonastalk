package com.jonastalk;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.jonastalk.auth.v1.repository.AccountListRepository;

@SpringBootTest
@ActiveProfiles("local")
class JonastalkAuthApiApplicationTests {

	@Test
	void contextLoads() {
	}

    @Autowired
    StringEncryptor jasyptStringEncryptor;

    @Autowired
    AccountListRepository accountListRepository;

    @Test
    public void jasyptEncryptTest() {
        System.out.println("@@@@@ TEST @@@@@ encryted string: " + jasyptStringEncryptor.encrypt("jonastalk"));
        System.out.println("@@@@@ TEST @@@@@ encryted string: " + jasyptStringEncryptor.encrypt("1234"));
    }

    @Test
    public void jasyptDecryptTest() {
    	System.out.println("@@@@@ TEST @@@@@ decryted string: " + jasyptStringEncryptor.decrypt("Inj6f4uWRyNr64LV+wP+v6ATYWi9Fx2T"));
        System.out.println("@@@@@ TEST @@@@@ decryted string: " + jasyptStringEncryptor.decrypt("rbehroO9rtReft7xWi8Pjw=="));
    }
    
    @Test
    public void dbTest() {
    	System.out.println("@@@@@ TEST @@@@@ userList:" + accountListRepository.findByUsername("admin"));
    }

}
