
package com.example.SPP.Repository;

import com.example.SPP.Model.SPP;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SPPRepository extends JpaRepository<SPP, Long> {

    Optional<SPP> findByTahun(String tahun);
}
