package com.akatsuki.pioms.notice.repository;


import com.akatsuki.pioms.notice.aggregate.Notice;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NoticeRepository extends JpaRepository<Notice,Integer>{

}
