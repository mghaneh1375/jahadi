$jarPath = Join-Path $PSScriptRoot "target/secured.jar"
$tempJar = Join-Path $env:TEMP "temp_$(Get-Date -Format 'yyyyMMddHHmmss').jar"
$env:APP_SAFE_START = "SECURE_WRAPPER"

# Decrypt the JAR using OpenSSL
try {
    & openssl enc -d -aes-256-cbc -pass pass:123 -pbkdf2 -in $jarPath -out $tempJar

    # Verify decryption was successful
    if (Test-Path $tempJar) {
        # Run the decrypted JAR
	$javaParams = @(
		"-agentlib:jdwp=transport=dt_socket,address=5005,suspend=n,server=y",
		"-Dfile.encoding=UTF-8",
    		"-Dspring.config.location=C:\Users\user\IdeaProjects\Jahadi\application-local.yml",
    		"-Dspring.profiles.active=local",
		"-DlicenseKey=:DwVO}s;uXSD]U11@kI06`x5MTwy@dP)",
		"-Dpassword=doXGBF5H885a'q%5g?bfa@M*R^!3es(0",
		"-DencryptionPassword=|)q,xeI3w4g@WtH[`7>}f6vN$Q3iY)[P",
    		"-jar",
    		$tempJar
	)
        Start-Process -NoNewWindow -Wait -FilePath "java" -ArgumentList $javaParams -RedirectStandardOutput "output.txt" -RedirectStandardError "error.txt"
        # Clean up temporary file
        Remove-Item $tempJar -Force
    } else {
        Write-Error "Failed to decrypt the JAR file"
        exit 1
    }
} catch {
    Write-Error "Error during decryption: $_"
    exit 1
}