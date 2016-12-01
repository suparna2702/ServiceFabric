<VirtualHost *:80>
	ServerAdmin webmaster@businessreach.com

	DocumentRoot /opt/similan/www
        ServerName businessreach.com

	ErrorLog /var/log/similan/demo/error-root.log

	# Possible values include: debug, info, notice, warn, error, crit,
	# alert, emerg.
	LogLevel warn

	CustomLog /var/log/similan/demo/access-root.log combined

        RewriteEngine on
#        RewriteCond %{HTTP_HOST} ^www\.businessreach\.com$ [NC]
        RewriteRule ^(.*)$ http://www.businessreach.com$1 [R=301,L]
</VirtualHost>
