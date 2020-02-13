package org.example.dao;

import org.example.model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role getRole(String roleName) {
        return (Role) entityManager
                .createQuery("from Role r where r.name =:roleName")
                .setParameter("roleName", roleName)
                .getSingleResult();
    }
}
