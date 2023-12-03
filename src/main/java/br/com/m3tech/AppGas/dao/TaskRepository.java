package br.com.m3tech.AppGas.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.m3tech.AppGas.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
