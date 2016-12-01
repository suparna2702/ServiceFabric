<VirtualHost *:80>
	ServerAdmin webmaster@admin.businessreach.com

	DocumentRoot /opt/similan/www
        ServerName admin.businessreach.com

	ErrorLog /var/log/similan/demo/error-admin.log

	# Possible values include: debug, info, notice, warn, error, crit,
	# alert, emerg.
	LogLevel warn

	CustomLog /var/log/similan/demo/access-admin.log combined

        <Location />
                AuthType Basic
                AuthName BR
                AuthUserFile /opt/similan/demo/users
                Satisfy All
                require valid-user
        </Location>

        ProxyRequests Off

        RewriteEngine On
        RewriteRule ^/$ http://admin.businessreach.com/admin/ [L,R=301]
 
        <Proxy *>
          Order deny,allow
          Allow from all
        </Proxy>

        ProxyPass /error-documents !
        ErrorDocument 503 /error-documents/manteniance.html
        Alias /error-documents /opt/similan/www
        AllowEncodedSlashes on

        ProxyPass / http://localhost:8080/
        ProxyPreserveHost On
        ProxyStatus On
</VirtualHost>
