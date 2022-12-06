package in.nic.ashwini.eForms.security.granters;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;

import in.nic.ashwini.eForms.exception.MfaRequiredException;
import in.nic.ashwini.eForms.service.MfaService;
import in.nic.ashwini.eForms.service.UtilityService;
import in.nic.ashwini.eForms.service.ValidationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PassappMobileTokenGranter extends AbstractTokenGranter {
	private static final String GRANT_TYPE = "passAppMobile";
	private final AuthenticationManager authenticationManager;
	private final MfaService mfaService;
	private final UtilityService utilityService;
	private final ValidationService validationService;

	public PassappMobileTokenGranter(AuthorizationServerEndpointsConfigurer endpointsConfigurer,
			AuthenticationManager authenticationManager, MfaService mfaService, UtilityService utilityService,
			ValidationService validationService) {
		super(endpointsConfigurer.getTokenServices(), endpointsConfigurer.getClientDetailsService(),
				endpointsConfigurer.getOAuth2RequestFactory(), GRANT_TYPE);
		this.authenticationManager = authenticationManager;
		this.mfaService = mfaService;
		this.utilityService = utilityService;
		this.validationService = validationService;
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
		Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
		if (!parameters.containsKey("mobile")) {
			throw new InvalidRequestException("Missing mobile number!!!");
		}
		String mobile = parameters.get("mobile");
		parameters.remove("mobile");

		Collection<GrantedAuthority> authorities = null;
		authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_PRE_AUTH"),
				new SimpleGrantedAuthority("ROLE_PASSAPP_MOBILE_USER"));
		Authentication userAuth = new UsernamePasswordAuthenticationToken(mobile, "", authorities);

		OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
		String message = "Please enter the OTP sent to mobile number ("+mobile+")";

		OAuth2AccessToken accessToken = getTokenServices()
				.createAccessToken(new OAuth2Authentication(storedOAuth2Request, userAuth));
		throw new MfaRequiredException(accessToken.getValue(), message, "");
	}
}
