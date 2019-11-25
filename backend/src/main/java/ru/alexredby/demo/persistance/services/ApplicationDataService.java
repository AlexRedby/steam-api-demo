package ru.alexredby.demo.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alexredby.demo.persistance.models.Application;
import ru.alexredby.demo.persistance.repositories.ApplicationRepository;

import javax.annotation.Nullable;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ApplicationDataService {

    private ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationDataService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Optional<Application> findById(Integer id) {
        return applicationRepository.findById(id);
    }

    public List<Application> findAllById(List<Integer> ids) {
        return applicationRepository.findAllById(ids);
    }

    public Application save(Application application) {
        return applicationRepository.save(application);
    }

    public List<Application> saveAll(List<Application> applications) {
        return applicationRepository.saveAll(applications);
    }
}
