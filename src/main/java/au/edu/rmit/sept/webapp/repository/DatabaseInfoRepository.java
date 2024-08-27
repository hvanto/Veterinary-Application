package au.edu.rmit.sept.webapp.repository;


import au.edu.rmit.sept.webapp.model.DatabaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseInfoRepository extends JpaRepository<DatabaseInfo, Long> {
}
