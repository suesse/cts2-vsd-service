package edu.mayo.cts2.framework.plugin.service.mat.profile.valuesetdefinition

import edu.mayo.cts2.framework.model.command.ResolvedFilter

import static org.junit.Assert.*

import javax.annotation.Resource
import javax.xml.transform.stream.StreamResult

import org.junit.Test

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.mat.loader.MatZipLoader
import edu.mayo.cts2.framework.plugin.service.mat.test.AbstractZipLoadingTestBase
import edu.mayo.cts2.framework.service.command.restriction.ValueSetDefinitionQueryServiceRestrictions
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQuery
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQueryService

class MatValueSetDefinitionQueryServiceTest extends AbstractZipLoadingTestBase {

	@Resource
	def ValueSetDefinitionQueryService service
	
	@Resource
	def MatZipLoader loader

	def marshaller = new DelegatingMarshaller()
	
	@Test
	void TestSetUp() {
		assertNotNull service
	}	
	
	@Test
	void TestQuerySize() {
		assertTrue service.getResourceSummaries(null as ValueSetDefinitionQuery,null,null).entries.size() > 10
	}
	
	@Test
	void TestIsPartialFalse() {
		def summaries = service.getResourceSummaries(null as ValueSetDefinitionQuery,null,new Page(maxToReturn:5,page:0))
		
		assertTrue summaries.entries.size() < 50
		
		assertFalse summaries.atEnd
	}
	
	@Test
	void TestQueryDefinitionsOfValueSet() {
		def summaries = service.getResourceSummaries(
			[
				getRestrictions : {
					def restrictions = new ValueSetDefinitionQueryServiceRestrictions()
					restrictions.setValueSet(ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.526.02.99"))

					restrictions
				},
				getFilterComponent : { new HashSet() }

			] as ValueSetDefinitionQuery, null, null)
		
		assertEquals 1, summaries.entries.size()
	}

	@Test
	void TestQueryDefinitionsByCreator() {
		def summaries = service.getResourceSummaries([
		    getRestrictions: {},
				getFilterComponent: {
					def filters = new HashSet<ResolvedFilter>()
					filters.add(new ResolvedFilter())
				}
		] as ValueSetDefinitionQuery, null, null)
	}
	
	@Test
	void TestIsPartialTrue() {
		def summaries = service.getResourceSummaries(null as ValueSetDefinitionQuery,null,new Page(maxToReturn:50,page:0))
		
		assertTrue summaries.entries.size() < 50
		
		assertTrue summaries.atEnd
	}
	
	@Test
	void TestValidXml() {
		def entries = service.getResourceSummaries(null as ValueSetDefinitionQuery,null,null).entries
		
		assertTrue entries.size() > 0
		
		entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}

}
