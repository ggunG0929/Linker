package Linker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Linker.model.File;

public interface FileRepository extends JpaRepository<File, Long> {
}
