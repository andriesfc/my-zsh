

function date() {
  if [ "$1" = "-I" ]; then
    command date "+%Y-%m-%dT%H:%M:%S%z"
  else
   command date "$@"
  fi
}

function append_to_path() {
  export PATH="$PATH:$1"
}

function current_dir_name()
{
    echo $(python3 -c "from pathlib import Path; print(Path('.').absolute().name)")
}

function lst() {

  local target="$1"

  if [ -z $target ]; then
    target="."
  fi 

   exa -l -h --group-directories-first -U -F --accessed --time-style long-iso "$target"
}

# Some interesting alias taken from https://www.thorsten-hans.com/5-types-of-zsh-aliases

# open any file with the following extensions usig vscode
alias -s {md,json,yaml,yml,xml,conf,ini}=code

# A function to drop all files and folders
# NB: Use with caution
function drop() {
  what_to_drop="$@"
  if [ -z $what_to_drop ]; then 
    print "please specify which folders and/or files to drop using:"
    print "drop <files-or-folders>"
    return
  fi  
  print "completly removing all files and folders."
  for todo in $@; do 
    print "from: $todo"
    (rm -fr "$todo")
  done 
}

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
  echo $(cd $path_to_resolve; pwd)  
}

function dice() { 
  java -jar "$HOME/Dev/dice-1.6.0.jar" --offline "$@"
}

function dk-ps() {
  docker ps --format "table {{.ID}}\t{{.Names}}" "$@"
}

# Short cut for docker compose -- because who cares about using 
# an artbitrary precision calculator, and typing oneself to death
function dc() {
  docker-compose "$@"
}

function dk() {
  docker "$@"
}