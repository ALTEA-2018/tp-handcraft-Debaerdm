package repositories;

import beans.PokemonType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PokemonTypeRepository {
    private List<PokemonType> pokemons;

    private static PokemonTypeRepository pokemonTypeRepository;

    public static PokemonTypeRepository getInstance() {
        if (pokemonTypeRepository == null) {
            pokemonTypeRepository = new PokemonTypeRepository();
        }
        return pokemonTypeRepository;
    }

    private PokemonTypeRepository() {
        try {
            var pokemonsStream = this.getClass().getResourceAsStream("/pokemons.json");
            var objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            var pokemonsArray = objectMapper.readValue(pokemonsStream, PokemonType[].class);
            System.out.println(Arrays.toString(pokemonsArray));
            this.pokemons = new ArrayList<>();
            this.pokemons.addAll(Arrays.asList(pokemonsArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PokemonType findPokemonById(int id) {
        System.out.println("Loading Pokemon information for Pokemon id " + id);

        return this.pokemons.stream()
                .filter(pokemonType -> pokemonType.getId() == id)
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }

    public PokemonType findPokemonByName(String name) {
        System.out.println("Loading Pokemon information for Pokemon name " + name);

        return this.pokemons.stream()
                .filter(pokemonType -> pokemonType.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }

    public List<PokemonType> findAllPokemon() {
        return this.pokemons;
    }

    public void addPokemon(PokemonType pokemonType) {
        if (pokemons.stream().anyMatch(pokemon ->
                pokemonType.getId() == pokemon.getId() && pokemonType.getName().equals(pokemon.getName())
        )) {
            throw new IllegalArgumentException("pokemon already exist");
        }

        System.out.println("Adding Pokemon : " + pokemonType.getName());

        this.pokemons.add(pokemonType);
    }
}
