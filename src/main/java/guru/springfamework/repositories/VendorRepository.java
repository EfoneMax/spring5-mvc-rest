package guru.springfamework.repositories;

import guru.springfamework.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    List<Vendor> findByName(String name);
}
