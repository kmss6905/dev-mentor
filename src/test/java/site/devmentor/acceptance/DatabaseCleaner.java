package site.devmentor.acceptance;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@ActiveProfiles("test")
public class DatabaseCleaner implements InitializingBean {

  @PersistenceContext
  private EntityManager entityManager;

  private List<String> tableNames;

  @Override
  public void afterPropertiesSet() {
    tableNames = entityManager.getMetamodel().getEntities().stream()
            .filter(entityType -> Objects.nonNull(entityType.getJavaType().getAnnotation(Entity.class)))
            .map(entityType -> entityType.getName().toUpperCase())
            .collect(Collectors.toList());

    tableNames = tableNames.stream().map(it -> {
      if (it.equals("USER")) {
        return "USERS";
      }
      if (it.equals("MENTORINFO")) {
        return "MENTOR_INFO";
      }
      if (it.equals("MENTORREQUEST")) {
        return "REQUEST";
      }
      if (it.equals("SCHEDULE")) {
        return "MENTORING_SCHEDULE";
      }
      if (it.equals("SCHEDULEDETAIL")) {
        return "MENTORING_SCHEDULE_DETAIL";
      }
      if (it.equals("REVIEW")) {
        return "MENTOR_REVIEWS";
      }
      return it;
    }).collect(Collectors.toList());
  }

  @Transactional
  public void execute() {
    entityManager.flush();
    entityManager.createNativeQuery("SET foreign_key_checks = 0;").executeUpdate();
    tableNames.forEach(tableName -> executeQueryWithTable(tableName));
    entityManager.createNativeQuery("SET foreign_key_checks = 1;").executeUpdate();
  }

  private void executeQueryWithTable(String tableName) {
    entityManager.createNativeQuery("TRUNCATE TABLE " + tableName + ";").executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1;").executeUpdate();
  }
}
