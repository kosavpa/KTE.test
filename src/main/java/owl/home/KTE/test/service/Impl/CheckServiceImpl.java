package owl.home.KTE.test.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import owl.home.KTE.test.model.check.Check;
import owl.home.KTE.test.repo.CheckRepository;
import owl.home.KTE.test.service.Interface.CheckService;

import java.util.List;
import java.util.NoSuchElementException;


@Component
public class CheckServiceImpl implements CheckService {
    @Autowired
    CheckRepository repository;

    @Override
    public Check checkById(long checkId) {
        return repository.findById(checkId).orElseThrow(
                () -> new IllegalArgumentException("Check with this id not found!")
        );
    }

    @Override
    public List<Check> checksByClientId(long clientId) {
        return repository.findCheckByClientId(clientId);
    }

    @Override
    public boolean deleteCheckById(long checkId) {
        repository.deleteById(checkId);

        return repository.existsById(checkId);
    }

    @Override
    public Check saveCheck(Check check) {
        return repository.save(check);
    }
}
