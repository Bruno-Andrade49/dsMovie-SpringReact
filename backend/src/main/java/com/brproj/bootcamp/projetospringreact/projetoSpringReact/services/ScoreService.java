package com.brproj.bootcamp.projetospringreact.projetoSpringReact.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brproj.bootcamp.projetospringreact.projetoSpringReact.Repository.MovieRepository;
import com.brproj.bootcamp.projetospringreact.projetoSpringReact.Repository.ScoreRepository;
import com.brproj.bootcamp.projetospringreact.projetoSpringReact.Repository.UserRepository;
import com.brproj.bootcamp.projetospringreact.projetoSpringReact.dto.MovieDTO;
import com.brproj.bootcamp.projetospringreact.projetoSpringReact.dto.ScoreDTO;
import com.brproj.bootcamp.projetospringreact.projetoSpringReact.entities.Movie;
import com.brproj.bootcamp.projetospringreact.projetoSpringReact.entities.Score;
import com.brproj.bootcamp.projetospringreact.projetoSpringReact.entities.User;

@Service
public class ScoreService {
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ScoreRepository scoreRepository;

	@Transactional
	public MovieDTO saveScore(ScoreDTO dto) {
		
		User usuario = userRepository.findByEmail(dto.getEmail());
		
		if(usuario == null) {
			usuario = new User();
			usuario.setEmail(dto.getEmail());
			usuario = userRepository.saveAndFlush(usuario);
		}
		
		Movie filme = movieRepository.findById(dto.getMovieId()).get();
		
		Score nota = new Score();
		nota.setMovie(filme);
		nota.setUser(usuario);
		nota.setValue(dto.getScore());
		nota = scoreRepository.saveAndFlush(nota);		
		
		double sum = 0.0;
		for (Score filmes: filme.getScores()) {
			sum += filmes.getValue();	
		}
		
		double media = sum / filme.getScores().size();
		
		filme.setScore(media);
		filme.setCount(filme.getScores().size());
		
		filme = movieRepository.save(filme);
		
		return new MovieDTO(filme);
		
		
	}
	
}
