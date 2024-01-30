function date() {
  if [ "$1" = "-I" ]; then
    command date "+%Y-%m-%dT%H:%M:%S%z"
  else
    command date "$@"
  fi
}

function append-to-path() {
  export PATH="$PATH:$1"
}

function current-dir-name() {
  echo $(python3 -c "from pathlib import Path; print(Path('.').absolute().name)")
}

# Some interesting alias taken from https://www.thorsten-hans.com/5-types-of-zsh-aliases

# open any file with the following extensions usig vscode
alias -s {md,json,yaml,yml,xml,conf,ini}=code

# Build a complete
function mkdirs() {
  dirs_to_make="$@"
  if [ -z $dirs_to_make ]; then
    print "please supply a list of folders to create."
    return
  fi
  for wanting_dir in $@; do
    mkdir -p $wanting_dir
  done
}

# Simple function to respolve an actual path to an absulte file path.
# NB: Does not resolve symnlinks!
function resolvepath() {
  path_to_resolve="$1"
  if [ -z $path_to_resolve ]; then
    path_to_resolve="."
  fi
  echo $(
    cd $path_to_resolve
    pwd
  )
}

function dice() {
  java -jar "$HOME/Local/dice/dice-1.7.0-optimized.jar" --offline "$@"
}

function dk-ps() {
  docker ps --format "table {{.ID}}\t{{.Names}}" "$@"
}

# Short cut for docker compose -- because who cares about using
# an artbitrary precision calculator, and typing oneself to death
function dc() {
  docker compose "$@"
}

function dk() {
  docker "$@"
}

function trim() {
  awk '{$1=$1};1'
}

# Taken from here: https://stackoverflow.com/a/32142458/255645
expandtilde() {

  local tilde_re='^(~[A-Za-z0-9_.-]*)(.*)'
  local path="$*"
  local pathSuffix=

  if [[ $path =~ $tilde_re ]]; then
    # only use eval on the ~username portion !
    path=$(eval echo ${BASH_REMATCH[1]})
    pathSuffix=${BASH_REMATCH[2]}
  fi

  echo "${path}${pathSuffix}"
}

function remove() {
  rm -frv "$@"
}

function current_branch() {
  echo "$(git rev-parse --abbrev-ref HEAD)"
}