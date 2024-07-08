# java-labs: Creating the environment for Java development and Git - 2023-2024

## Netcat (nc)
- Windows OS
    - Download MobaXterm from [here](https://download.mobatek.net/2362023122033030/MobaXterm_Portable_v23.6.zip) and extract the content. No installation is needed. Netcat is available if you click on "Start local terminal".

- Linux OS
    - Netcat should already be installed on your system.

## IntelliJ IDEA Community Edition

- Download IntelliJ IDEA from [here](https://www.jetbrains.com/es-es/idea/download/) and install it using default options.

## Git 

- Download Git from [here](https://git-scm.com/downloads) and install it using default options

> Note that, for instance, in Ubuntu systems you could download and install it by simply executing the following:
  
```shell
    sudo apt-get install git
```

- Basic configuration
    - In Windows OS, the following commands should be executed inside git-bash (`$GIT_HOME/git-bash.exe`):
    
```shell
    git config --global user.email "<user-login>@udc.es"
    git config --global user.name "<user-name>"
```

> The following line illustrates how to set Sublime as the Git default editor, but you can use any other editor installed in your OS (you can download Sublime Text editor from [here](https://www.sublimetext.com/download))
      
```shell
    >Windows OS
	git config --global core.editor "'C:\Program Files\Sublime Text\sublime_text.exe' -w"
	
    >Linux OS
	git config --global core.editor "subl -w"
```

### Creation and configuration of SSH Keys

- From the git-bash interpreter in Windows OS systems or from the shell in Linux and Mac OS systems
> Generate SSH keys in the default path ($HOME/.ssh or ~/.ssh, as appropriate) and with default names
      
```shell
    ssh-keygen -t rsa -b 4096 -C "<user-login>@udc.es"
```    
    
- Open the browser and navigate to [https://github.com/settings/keys](https://github.com/settings/keys)
- In the "Key" field, copy the public key, i.e., content of file `$HOME/.ssh/id_rsa.pub`
- In the "Title" field, specify a name for the key
- Click on the "Add key" button

- Try SSH connectivity against the Git server and add it to the list of known hosts
  > Answer "yes" to the question "Are you sure you want to continue connecting (yes/no)?"
   
```shell
    ssh -T git@github.com
```   

## Creating your project

### Create Git repository in Github web site

1. Open the browser, navigate to https://github.com/ and log in.
2. Click on the assignment URL available (https://classroom.github.com/...) in the lab statement.
3. Create a new team defining the team name as follows: \<your lab group\>-\<your UDC login\> (for instance, 1.3-john.doe) 
4. Accept java-labs assignment. 
5. Refresh browser after some seconds.
6. Search for a new email from Github where you are invited to join the GEI-Red-6146010172324 organization.
7. Click on the link to join the organization.
8. Authenticate your account logging into UDC single sign-on (SSO) provider.
9. Join the organization.
10. Click on your repository link.   

### Authorize SSH keys for access organization

1. Open the browser and navigate to [https://github.com/settings/keys](https://github.com/settings/keys)
2. Click on the "Configure SSO" button next to the SSH keys you have created.
3. Search for the GEI-Red-6146010172324 organization and click on the "Authorize" button associate to our organization.
4. Single sign-on to UDC is needed to complete the authorization.

### Initializing your local Git repository

```shell
	git clone git@github.com:GEI-Red-614G010172324/java-labs-<team-name>.git
```

 NOTE that &lt;team-name&gt; must be changed by your team name, that is, &lt;your lab group&gt;-&lt;your UDC login&gt;.

### Working on your Git repository

- These are the basic commands to use when a change has been made in your project. Although "git status" is not mandatory, it can be very useful. The same happens with "git log". 

```shell
	cd java-labs-<team-name>
	git status
	git add . 
	git commit -m "commit message"	
	git push origin main
	git log
```

-  In order to create a tag, previous commands must be followed by these ones. NOTE that it is important to add -a option to create an annotated tag (otherwise a lightweight tag will be created).


```shell	
	git tag -a <tag-name> -m "tag message"	
	git push origin <tag-name>
```

- If necessary, in order to delete a tag, these commands delete the tag locally and remotely, respectively:

```shell	
	git tag -d <tag-name> 
	git push --delete origin <tag-name>
```

### Load the project in IntelliJ IDEA

- Click on "File" > "New" > "Project from Existing Sources" menu option.
- Select "java-labs-&lt;team-name&gt;" folder as the folder to import.
- Click on "Ok" button.
- Choose "Create project from existing sources" and click on "Next" button. 
- Set "java-labs-&lt;team-name&gt;" as "Project Name". The default values are correct for the remaining fields. 
NOTE that IntelliJ IDEA project should be created within java-labs-&lt;team-name&gt; (do not change the default value 
for the project location). This way a .iml file, an out/ folder (with .class files) and a .idea folder (with IntelliJ 
configuration) will appear within java-labs-&lt;team-name&gt;.
- Mark src folder and click on "Next" button. 
- Click on "Next" button until "Finish" button will appear, and click on "Finish" button.

### Using .gitignore
- Do not remove the .gitignore file from your repository.
- Do not modify the .gitignore file. Its content should be: 
```shell
# Target directories.
target/

# Eclipse configuration.
.classpath
.project
.settings/

# IntelliJ IDEA configuration.
.idea/
/out/
*.iml

# Netbeans configuration.
/nbproject/private/
/dist/
/build/
```
