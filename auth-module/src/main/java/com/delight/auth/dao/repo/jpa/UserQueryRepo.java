package com.delight.auth.dao.repo.jpa;

import com.delight.auth.api.model.UserPublicInfo;
import com.delight.auth.api.model.UserViewInfo;
import com.delight.gaia.jpa.repo.CustomRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class UserQueryRepo extends CustomRepo {
    public UserQueryRepo(@Qualifier("authEntityTemplate") R2dbcEntityTemplate
                                 r2dbcEntityTemplate) {
        super(r2dbcEntityTemplate);
    }

    public Flux<UserViewInfo> listUserViewInfo(Set<Long> ids) {
        StringBuilder query = new StringBuilder("select  id, has_avatar,firstname, lastname from account where id in ($1");
        List<Object> params = new ArrayList<>(ids.size());
        for (Long id : ids) {
            params.add(id);
            query.append(",$").append(params.size());
        }
        query.append(")");
        return query(query.toString(), params, UserViewInfo.class);
    }

    public Flux<UserViewInfo> listUserViewInfo(List<Long> ids) {
        StringBuilder query = new StringBuilder("select  id, has_avatar,firstname, lastname from account where id in ($1");
        List<Object> params = new ArrayList<>(ids.size());
        params.add(ids.remove(0));
        for (Long id : ids) {
            params.add(id);
            query.append(",$").append(params.size());
        }
        query.append(")");
        return query(query.toString(), params, UserViewInfo.class);
    }

    public Mono<UserViewInfo> listUserViewInfo(Long id) {
        StringBuilder query = new StringBuilder("select    has_avatar,firstname, lastname from account where id =$1");
        List<Object> params = new ArrayList<>(1);
        params.add(id);
        return queryOne(query.toString(), params, UserViewInfo.class).map(userViewInfo -> {
            userViewInfo.setId(id);
            return userViewInfo;
        });
    }

    public Flux<UserPublicInfo> searchUser(String keyword, int page, int pageSize) {
        StringBuilder query = new StringBuilder("select  id, has_avatar,firstname, lastname,gender, birthday from account where email= $1 or phone =$1 or name @@ to_tsquery($1) limit  $2 offset $3");
        List<Object> params = new ArrayList<>(3);
        params.add(keyword);
        params.add(pageSize);
        params.add(page);
        return query(query.toString(), params, UserPublicInfo.class);
    }

}
