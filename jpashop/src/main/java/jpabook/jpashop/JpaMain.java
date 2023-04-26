package jpabook.jpashop;

import jpabook.jpashop.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        Member member = new Member();
        member.setId(10L);
        member.setName("유저1");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // 1차 캐시에 저장
       em.persist(member);

       // 1차 캐시에서 조회
        Member findMember = em.find(Member.class, 10L);

        System.out.println("findMember = " + findMember.getId());
        System.out.println("findMember = " + findMember.getName());

    }
}
