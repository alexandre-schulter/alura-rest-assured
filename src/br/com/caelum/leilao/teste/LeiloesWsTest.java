package br.com.caelum.leilao.teste;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;

import br.com.caelum.leilao.modelo.Leilao;
import br.com.caelum.leilao.modelo.Usuario;

public class LeiloesWsTest {

	@Before
	public void before() {
		
	}
	
	@Test
	public void deveRetornarLeilaoPeloId() {
		JsonPath path = given()
				.header("Accept", "application/json")
				.parameter("leilao.id", 1)
				.get("/leiloes/show")
				.andReturn().jsonPath();
		System.out.println(path.getString("leilao"));
	}
	

    @Test
    public void deveRetornarUmLeilao() {
        JsonPath path = given()
                .parameter("leilao.id", 1)
                .header("Accept", "application/json")
                .get("/leiloes/show")
                .andReturn().jsonPath();

        Leilao leilao = path.getObject("leilao", Leilao.class);

        Usuario mauricio = new Usuario(1l, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");
        Leilao esperado = new Leilao(1l, "Geladeira", 800.0, mauricio, false);

        assertEquals(esperado, leilao);
    }
    
    @Test
    public void deveRetornaTotalLeiloes() {
    	XmlPath path = given()
    			.header("Accept", "application/xml")
    			.get("/leiloes/total")
    			.andReturn().xmlPath();
    	
    	//Integer total = path.getInt("");
    	int total = path.getInt("int");

    	assertEquals(2, total);
    }
    
    @Test
    public void deveAdicionarUmLeilao() {
    	Usuario mauricio = new Usuario(2L, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");
    	Leilao novoLeilao = new Leilao(5L, "Oportunidade 2", 1.0, mauricio, true);
    	
    	XmlPath path = given().header("Accept", "application/xml")
    			.contentType("application/xml")
    			.body(novoLeilao)
    			.expect().statusCode(200)
    			.when().post("/leiloes")
    			.andReturn().xmlPath();
    	
    	Leilao leilao = path.getObject("leilao", Leilao.class);
    	assertEquals("Oportunidade 2", leilao.getNome());
    	
		given().contentType("application/xml").body(leilao)
			.expect().statusCode(200)
			.when().delete("/leiloes/deletar")
			.andReturn().asString();
    }
    
}
