package repositories;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PokemonTypeRepositoryTest {

    @Test
    void findPokemonById_with25_shouldReturnPikachu(){
        var pikachu = PokemonTypeRepository.getInstance().findPokemonById(25);
        assertNotNull(pikachu);
        assertEquals("pikachu", pikachu.getName());
        assertEquals(25, pikachu.getId());
    }

    @Test
    void findPokemonById_with145_shouldReturnZapdos(){
        var zapdos = PokemonTypeRepository.getInstance().findPokemonById(145);
        assertNotNull(zapdos);
        assertEquals("zapdos", zapdos.getName());
        assertEquals(145, zapdos.getId());
    }

    @Test
    void findPokemonByName_withEevee_shouldReturnEevee(){
        var eevee = PokemonTypeRepository.getInstance().findPokemonByName("eevee");
        assertNotNull(eevee);
        assertEquals("eevee", eevee.getName());
        assertEquals(133, eevee.getId());
    }

    @Test
    void findPokemonByName_withMewTwo_shouldReturnMewTwo(){
        var mewtwo = PokemonTypeRepository.getInstance().findPokemonByName("mewtwo");
        assertNotNull(mewtwo);
        assertEquals("mewtwo", mewtwo.getName());
        assertEquals(150, mewtwo.getId());
    }

    @Test
    void findAllPokemon_shouldReturn151Pokemons(){
        var pokemons = PokemonTypeRepository.getInstance().findAllPokemon();
        assertNotNull(pokemons);
        assertEquals(151, pokemons.size());
    }

}