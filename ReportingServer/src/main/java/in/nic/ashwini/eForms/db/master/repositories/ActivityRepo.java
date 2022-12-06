package in.nic.ashwini.eForms.db.master.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.nic.ashwini.eForms.db.master.entities.Activity;

public interface ActivityRepo extends JpaRepository<Activity, Integer>{

	List<Activity> findTop10ByEmailOrderByDatetimeDesc(String email);
	List<Activity> findByEmailOrderByDatetimeDesc(String email);
}
