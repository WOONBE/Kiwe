package com.d205.KIWI_Backend.kiosk.repository;

import com.d205.KIWI_Backend.kiosk.domain.Kiosk;
import com.d205.KIWI_Backend.member.domain.Member;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface KioskRepository extends JpaRepository<Kiosk, Integer> {

    List<Kiosk> findByMember(Member member);

    List<Kiosk> findByMemberId(Integer memberId);

    @Query(value = "SELECT k.kiosk_order_num " +
            "FROM (SELECT kiosk_id, ROW_NUMBER() OVER(ORDER BY kiosk_id) AS kiosk_order_num " +
            "      FROM kiosk " +
            "      WHERE owner_id = :ownerId) AS k " +
            "WHERE k.kiosk_id = :kioskId", nativeQuery = true)
    Long findKioskOrderNumberByOwnerIdAndKioskId(@Param("ownerId") Long ownerId,
                                                    @Param("kioskId") Long kioskId);


}
