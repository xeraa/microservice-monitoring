package net.xeraa.backend;

import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Integer> {

    Iterable<Person> findByNameLike(String name);
}
