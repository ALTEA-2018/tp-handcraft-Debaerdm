package controllers;
import annotations.Controller;
import annotations.RequestMapping;
import beans.PokemonType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repositories.PokemonTypeRepository;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PokemonTypeControllerTest {

    @InjectMocks
    PokemonTypeController controller;

    @Mock
    PokemonTypeRepository pokemonRepository;

    @BeforeEach
    void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getPokemon_shouldRequireAParameter(){
        var exception = assertThrows(IllegalArgumentException.class, () -> controller.getPokemon(null));
        assertEquals("parameters should not be empty", exception.getMessage());
    }

    @Test
    void getPokemon_shouldRequireAKnownParameter(){
        var parameters = Map.of("test", new String[]{"25"});
        var exception = assertThrows(IllegalArgumentException.class, () -> controller.getPokemon(parameters));
        assertEquals("unknown parameter", exception.getMessage());
    }

    @Test
    void getPokemon_withAnIdParameter_shouldReturnAPokemon() throws IOException {
        var objectMapper = new ObjectMapper();
        var pikachu = new PokemonType();
        pikachu.setId(25);
        pikachu.setName("pikachu");
        when(pokemonRepository.findPokemonById(25)).thenReturn(pikachu);

        var parameters = Map.of("id", new String[]{"25"});
        var json = controller.getPokemon(parameters);
        PokemonType pokemon = objectMapper.readValue(json, PokemonType.class);
        assertNotNull(pokemon);
        assertEquals(25, pokemon.getId());
        assertEquals("pikachu", pokemon.getName());
    }

    @Test
    void getPokemon_withANameParameter_shouldReturnAPokemon() throws IOException {
        var objectMapper = new ObjectMapper();
        var zapdos = new PokemonType();
        zapdos.setId(145);
        zapdos.setName("zapdos");
        when(pokemonRepository.findPokemonByName("zapdos")).thenReturn(zapdos);

        var parameters = Map.of("name", new String[]{"zapdos"});
        var json = controller.getPokemon(parameters);
        PokemonType pokemon = objectMapper.readValue(json, PokemonType.class);
        assertNotNull(pokemon);
        assertEquals(145, pokemon.getId());
        assertEquals("zapdos", pokemon.getName());
    }

    @Test
    void pokemonTypeController_shouldBeAnnotated(){
        var controllerAnnotation =
                PokemonTypeController.class.getAnnotation(Controller.class);
        assertNotNull(controllerAnnotation);
    }

    @Test
    void getPokemon_shouldBeAnnotated() throws NoSuchMethodException {
        var getPokemonMethod =
                PokemonTypeController.class.getDeclaredMethod("getPokemon", Map.class);
        var requestMappingAnnotation =
                getPokemonMethod.getAnnotation(RequestMapping.class);

        assertNotNull(requestMappingAnnotation);
        assertEquals("/pokemon", requestMappingAnnotation.uri());
    }

}