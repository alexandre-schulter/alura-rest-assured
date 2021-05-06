package br.com.caelum.leilao.teste;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;

import br.com.caelum.leilao.modelo.Usuario;

public class UsuariosWsTest {

	private Usuario esperado1;
	private Usuario esperado2;
	
	@Before
	public void before() {
		esperado1 = new Usuario(1L, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");
        esperado2 = new Usuario(2L, "Guilherme Silveira", "guilherme.silveira@caelum.com.br");
	}
	
	@Test
    public void deveRetornarListaDeUsuarios() {
        //XmlPath path = get("/usuarios?_format=xml").andReturn().xmlPath();
		XmlPath path = given().header("Accept", "application/xml").get("/usuarios").andReturn().xmlPath();
		
        //Usuario usuario1 = path.getObject("list.usuario[0]", Usuario.class);
        //Usuario usuario2 = path.getObject("list.usuario[1]", Usuario.class);

        List<Usuario> usuarios = path.getList("list.usuario", Usuario.class);

        assertEquals(esperado1, usuarios.get(0));
        assertEquals(esperado2, usuarios.get(1));
    }
	
	@Test
	public void deveRetornarUsuarioPeloId() {
		JsonPath path = given()
				.header("Accept", "application/json")
				.queryParam("usuario.id", 1)
				.get("/usuarios/show")
				.andReturn().jsonPath();
		Usuario usuario = path.getObject("usuario", Usuario.class);
		assertEquals(esperado1, usuario);
	}
	
	@Test
	public void deveAdicionarUmUsuario() {
		Usuario novoUsuario = new Usuario("Alexandre Schulter", "xx@teste.com");
		
		XmlPath path = given()
				.header("Accept", "application/xml")
				.contentType("application/xml")
				.body(novoUsuario)
			.expect()
				.statusCode(200)
			.when()
				.post("/usuarios")
			.andReturn()
				.xmlPath();
			
		Usuario resposta = path.getObject("usuario", Usuario.class);
		
		assertEquals("Alexandre Schulter", resposta.getNome());
		
		given()
				.contentType("application/xml")
				.body(resposta)
			.expect()
				.statusCode(200)
			.when()
			.delete("/usuarios/deleta")
				.andReturn().asString();
	}
	
}
