/*package com.ObraSmart.GestionDeLogin.Service;

import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {

    private final Set<String> blacklist = new HashSet<>();

    public void blacklistToken(String token) {
        blacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }
}*/
// Este endpoint permite cerrar sesión invalidando el token actual.
//Con JWT sin estado, el backend no almacena sesiones, pero podemos usamos lista negra temporal (en memoria ).
package com.ObraSmart.GestionDeLogin.Service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {
    private final Set<String> blacklist = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void blacklist(String token) {
        if (token != null) blacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return token != null && blacklist.contains(token);
    }


}
