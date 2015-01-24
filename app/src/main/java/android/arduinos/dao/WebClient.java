package android.arduinos.dao;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientRootUrl;
import org.androidannotations.api.rest.RestClientSupport;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

@Rest(converters = {MappingJacksonHttpMessageConverter.class})
public interface WebClient extends RestClientRootUrl, RestClientSupport {

  // 1 nombre aléatoire dans l'intervalle [a,b]
  @Get("/{a}/{b}")
  public AleaResponse getAlea(int a, int b);
}
