package com.reneekbartlett.verisimilar.core.templates.resolver.registry;

import com.reneekbartlett.verisimilar.core.templates.resolver.UsernameTemplatesResolver;

public class TemplatesResolverRegistry {
    /***
     * 
     */

     private final UsernameTemplatesResolver usernameTemplatesResolver;

     public TemplatesResolverRegistry(UsernameTemplatesResolver usernameTemplatesResolver) {
         this.usernameTemplatesResolver = usernameTemplatesResolver;
     }

     public UsernameTemplatesResolver username() {
         return usernameTemplatesResolver;
     }
}
