package br.org.studio.tool;

import java.util.HashMap;
import java.util.Map;

import br.org.studio.tool.base.repository.Repository;
import br.org.studio.tool.base.repository.configuration.RepositoryConfiguration;
import br.org.studio.tool.mongodb.repository.MongoRepository;

public class RepositoryManagerFacade {

    private Map<String, Repository> repositories;

    public RepositoryManagerFacade() {
        repositories = new HashMap<>();
    }

    private Repository getRepository(RepositoryConfiguration configuration) {
        return new MongoRepository(configuration);
    }

    public void createRepository(RepositoryConfiguration configuration) throws Exception {
        Repository repository = getRepository(configuration);
        repository.initialize();

        repositories.put(configuration.getName(), repository);
    }

    public void deleteRepository(RepositoryConfiguration configuration) throws Exception {
        repositories.get(configuration.getName()).delete();
    }

    public void connectRepository(RepositoryConfiguration configuration) {
        Repository repository = repositories.get(configuration.getName());
        repository.load();
    }

    public Boolean isRepositoryAccessible(RepositoryConfiguration configuration) {
        Repository repository = getRepository(configuration);
        return repository.isAccessible();
    }

}