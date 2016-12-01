<VirtualHost *:80>
	ServerAdmin webmaster@businessreach.com

	DocumentRoot /opt/similan/www
        ServerName www.businessreach.com

	ErrorLog /var/log/similan/demo/error-portal.log

	# Possible values include: debug, info, notice, warn, error, crit,
	# alert, emerg.
	LogLevel warn

	CustomLog /var/log/similan/demo/access-portal.log combined

	<Location />
		AuthType Basic
		AuthName BR
		AuthUserFile /opt/similan/demo/users
		Satisfy All
		require valid-user
	</Location>
        
        ProxyRequests Off
        
        ProxyPass /error-documents !
        ErrorDocument 503 /error-documents/manteniance.html
        Alias /error-documents /opt/similan/www
        AllowEncodedSlashes On

	ProxyPass / http://localhost:8080/
        ProxyPreserveHost On
        ProxyStatus On
        
</VirtualHost>
