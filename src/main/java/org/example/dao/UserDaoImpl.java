package org.example.dao;

import org.example.model.User;
import org.example.model.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserDto> getUsers() {
        List<User> userList = (List<User>) entityManager.createQuery("from User").getResultList();
        List<UserDto> userDtoList = userList.stream().map(UserDto::new).collect(Collectors.toList());
        return userDtoList;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (User) entityManager
                .createQuery("from User u where u.username =:username")
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public boolean updateUser(User user) {
        try {
            entityManager.merge(user);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveUser(User user) {
        try {
            entityManager.persist(user);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteUser(long id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }
}
