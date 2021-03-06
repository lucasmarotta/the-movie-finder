package br.dcc.ufba.themoviefinder.entities.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.dcc.ufba.themoviefinder.entities.models.LodCacheRelation;
import br.dcc.ufba.themoviefinder.entities.models.LodRelationId;

@Repository
public interface LodRelationRepository extends JpaRepository<LodCacheRelation, LodRelationId>
{
	List<LodCacheRelation> findByIdIn(List<LodRelationId> lodRelationIds);
	
	@Query(nativeQuery=true, value="SELECT * FROM lod_cache_relation l WHERE resource1 IN :r1 AND resource2 IN :r2")
	List<LodCacheRelation> findByIdResource1InAndIdResource2In(@Param("r1") List<String> resources1, @Param("r2") List<String> resources2);
	
	@Query(nativeQuery=true, value="SELECT * FROM lod_cache_relation l ORDER BY RAND() LIMIT 100")
	List<LodCacheRelation> findRandomTop100();
}
