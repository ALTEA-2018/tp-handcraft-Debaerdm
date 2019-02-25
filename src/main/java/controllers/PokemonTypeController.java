package controllers;

import annotations.Controller;
import annotations.RequestMapping;
import beans.PokemonType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import repositories.PokemonTypeRepository;

import java.io.IOException;
import java.util.Map;

@Controller
public class PokemonTypeController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(method = "GET", uri = "/pokemon")
    public String getPokemon(Map<String, String[]> parameters) throws JsonProcessingException {
        if (parameters == null || parameters.isEmpty()) {
            throw new IllegalArgumentException("parameters should not be empty");
        }

        if(parameters.keySet().stream().noneMatch(key -> (key.equals("id") || key.equals("name")))) {
            throw new IllegalArgumentException("unknown parameter");
        }

        PokemonType pokemonType = null;

        if (parameters.containsKey("id")) {
            pokemonType = PokemonTypeRepository.getInstance().findPokemonById(Integer.parseInt(parameters.get("id")[0]));
        }

        if (parameters.containsKey("name")) {
            pokemonType = PokemonTypeRepository.getInstance().findPokemonByName(parameters.get("name")[0]);
        }

        return this.objectMapper.writeValueAsString(pokemonType);
    }

    @RequestMapping(method = "POST", uri = "/pokemon")
    public Integer addPokemon(String pokemon) throws IOException {
        if (pokemon == null) {
            throw new IllegalArgumentException("parameters should not be empty");
        }

        PokemonType pokemonType = this.objectMapper.readValue(pokemon, PokemonType.class);

        PokemonTypeRepository.getInstance().addPokemon(pokemonType);

        return 200;
    }

    @RequestMapping(method = "GET", uri = "/pokemons")
    public String getPokemons() throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(PokemonTypeRepository.getInstance().findAllPokemon());
    }
}
