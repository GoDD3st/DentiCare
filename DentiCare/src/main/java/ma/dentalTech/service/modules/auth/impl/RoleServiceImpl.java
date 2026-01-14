package ma.dentalTech.service.modules.auth.impl;

import ma.dentalTech.entities.Role.Role;
import ma.dentalTech.service.modules.auth.api.RoleService;

import java.util.List;
import java.util.Optional;

public class RoleServiceImpl implements RoleService {
    @Override
    public List<Role> findAll() throws Exception {
        return List.of();
    }

    @Override
    public Optional<Role> findByID(Long aLong) throws Exception {
        return Optional.empty();
    }

    @Override
    public Role create(Role item) {
        return null;
    }

    @Override
    public Role update(Long aLong, Role item) {
        return null;
    }

    @Override
    public Role delete(Role item) {
        return null;
    }

    @Override
    public void deleteByID(Long aLong) {

    }
}
