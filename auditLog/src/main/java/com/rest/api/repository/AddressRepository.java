package com.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rest.api.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
