package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.RoleEntity;
import com.example.chibiotaku.domain.enums.RoleUserEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class RoleRepositoryTests {

    @Autowired
    private RoleRepository underTest;

    @BeforeEach
    void setUp(){
        RoleEntity roleTest1 = new RoleEntity()
                .setRole(RoleUserEnum.USER);
        RoleEntity roleTest2 = new RoleEntity()
                .setRole(RoleUserEnum.ADMIN);
        this.underTest.saveAll(List.of(roleTest1,roleTest2));
    }

    @AfterEach
    void tearDown(){
        this.underTest.deleteAll();
    }



    @Test
    void findRoleEntityByRole_ShouldReturnCorrectEntities(){
        var roleEntity = this.underTest.findRoleEntityByRole(RoleUserEnum.USER);
        Assertions.assertTrue(roleEntity.isPresent());
        Assertions.assertEquals(roleEntity.get().getRole(),RoleUserEnum.USER);

        roleEntity = this.underTest.findRoleEntityByRole(RoleUserEnum.ADMIN);
        Assertions.assertTrue(roleEntity.isPresent());
        Assertions.assertEquals(roleEntity.get().getRole(),RoleUserEnum.ADMIN);
    }

}