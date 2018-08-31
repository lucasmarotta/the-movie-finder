package br.dcc.ufba.semrecsys.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.dcc.ufba.semrecsys.models.Movie;
import br.dcc.ufba.semrecsys.models.User;
import br.dcc.ufba.semrecsys.utils.CosineSimilarity;
import br.dcc.ufba.semrecsys.utils.DocVector;

@Service
public class UserMovieSimilarityService 
{
	@Autowired
	private TfIdfService tfIdfService;
	private boolean extendedMode;
	
	public UserMovieSimilarityService()
	{
		extendedMode = true;
	}
	
	public boolean isExtendedMode() 
	{
		return extendedMode;
	}

	public void setExtendedMode(boolean extendedMode) 
	{
		this.extendedMode = extendedMode;
	}

	public double getSimilarityFromMovie(Movie movie1, Movie movie2)
	{
		if(extendedMode) {
			return getSimilarity(movie1.getExtendedTokensList(), movie2.getExtendedTokensList());
		} else {
			return getSimilarity(movie1.getTokensList(), movie2.getTokensList());
		}
	}
	
	public double getSimilarityFromUser(User user, Movie movie)
	{
		List<String> userTokens = new ArrayList<String>();
		for(Movie userMovie : user.getMovies()) {
			if(extendedMode) {
				userTokens.addAll(userMovie.getExtendedTokensList());
			} else {
				userTokens.addAll(userMovie.getTokensList());
			}
		}
		if(extendedMode) {
			return getSimilarity(userTokens, movie.getExtendedTokensList());
		} else {
			return getSimilarity(userTokens, movie.getTokensList());
		}
	}
	
	public double getSimilarity(List<String> queryTokens, List<String> docTokens)
	{
		DocVector queryVector = new DocVector(queryTokens);
		DocVector docVector = new DocVector(queryTokens);
		String[] uniqueTerms = (String[]) queryVector.vectorTerms.keySet().toArray(new String[queryVector.vectorTerms.size()]);
		List<Float> queryTfIdfList = null;
		List<Float> docTfIdfList = null;
		
		if(extendedMode) {
			queryTfIdfList = tfIdfService.getBulkTfIdfExtended(queryTokens, uniqueTerms);
			docTfIdfList = tfIdfService.getBulkTfIdfExtended(docTokens, uniqueTerms);
		} else {
			queryTfIdfList = tfIdfService.getBulkTfIdf(queryTokens, uniqueTerms);
			docTfIdfList = tfIdfService.getBulkTfIdf(docTokens, uniqueTerms);
		}
		
		int size = queryTfIdfList.size();
		for (int i = 0; i < size; i++) {
			queryVector.setVectorValue(uniqueTerms[i], queryTfIdfList.get(i));
			docVector.setVectorValue(uniqueTerms[i], docTfIdfList.get(i));
		}
		
		/*
		for (Map.Entry<String, Integer> entry : queryVector.vectorTerms.entrySet()) {
			if(entry.getKey() != null && entry.getKey().length() > 0) {
				float queryTfIdf, docTfIdf = 0f;
				if(extendedMode) {
					queryTfIdf = tfIdfService.getTfIdfExtended(queryTokens, entry.getKey());
					docTfIdf = tfIdfService.getTfIdfExtended(docTokens, entry.getKey());
				} else {
					queryTfIdf = tfIdfService.getTfIdf(queryTokens, entry.getKey());
					docTfIdf = tfIdfService.getTfIdf(docTokens, entry.getKey());
				}
				queryVector.setVectorValue(entry.getKey(), queryTfIdf);
				docVector.setVectorValue(entry.getKey(), docTfIdf);
			}
		}
		*/
		return CosineSimilarity.getSimilarity(queryVector, docVector);
	}
}
