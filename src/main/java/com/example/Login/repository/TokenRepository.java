package com.example.Login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Login.model.Omnibus;
import com.example.Login.model.Token;

@Repository public interface TokenRepository extends JpaRepository<Token,
Integer>{ 
	
	
	
}



