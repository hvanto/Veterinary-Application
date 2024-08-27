package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.DatabaseInfo;
import au.edu.rmit.sept.webapp.repository.DatabaseInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseInfoService {

    @Autowired
    private DatabaseInfoRepository repository;

    public DatabaseInfo saveDatabaseInfo(String status) {
        DatabaseInfo info = new DatabaseInfo();
        info.setConnectionStatus(status);
        return repository.save(info);
    }

    public DatabaseInfo getDatabaseInfo(Long id) {
        return repository.findById(id).orElse(null);
    }
}
