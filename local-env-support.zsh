
localEnvFile=./.env
localEnvExtenFile=./.env.sh

if [ -f $localEnvFile ]; then
    if [ -f $localEnvExtenFile ]; then
        source "$localEnvExtenFile"
    fi
fi
