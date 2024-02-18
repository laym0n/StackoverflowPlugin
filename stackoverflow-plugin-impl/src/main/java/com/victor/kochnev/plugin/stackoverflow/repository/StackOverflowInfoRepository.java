package com.victor.kochnev.plugin.stackoverflow.repository;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StackOverflowInfoRepository extends JpaRepository<StackOverflowInfo, UUID> {
}
