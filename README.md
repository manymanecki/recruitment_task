# Few words
Thank you very much for giving me opportunity to take part in this recruitment step, that was lots of fun.

# Implemented features
[X] - Summary import
[X] - Description import
[X] - Priority import
[X] - Status import
[X] - Comments import
[X] - Basic error handling
[X] - Custom logger
[X] - Custom parser
[X] - Custom properties reader

# Additional dependencies
In order to not reinvent the wheel once again, I have decided to import the following libraries:
- gson (JSON parsing)
- snakeyaml (yaml parsing)

# How to boot locally?
1. Fork this repository
2. Install required dependencies
3. Create application.yaml inside resources folder
4. Structure the configuration file as required, that is:

```
jira:
  username: (username / email)
  token: (API token to the JIRA instance)
  project:
    url: (URL to JIRA instance)
    source: (Project KEY from which to import tickets, eg. KAN / CIB etc.)
    destination: (same as above, but to which import)
```

5. Run the main class and enjoy:)

Do not hesitate to get in touch with me @ jakub.pawlowski00@gmail.com
