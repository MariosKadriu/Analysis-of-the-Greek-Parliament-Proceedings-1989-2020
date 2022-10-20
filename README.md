# Analysis of the Greek Parliament Proceedings 1989 – 2020

## Table of Contents

[Description](https://github.com/MariosKadriu/Analysis-of-the-Greek-Parliament-Proceedings-1989-2020#-description)
 
[Dataset](https://github.com/MariosKadriu/Analysis-of-the-Greek-Parliament-Proceedings-1989-2020#-dataset)

[Installation](https://github.com/MariosKadriu/Analysis-of-the-Greek-Parliament-Proceedings-1989-2020#%EF%B8%8F-installation)

[Data Preprocessing](https://github.com/MariosKadriu/Analysis-of-the-Greek-Parliament-Proceedings-1989-2020#-data-preprocessing)

[Different Topics Across The Years](https://github.com/MariosKadriu/Analysis-of-the-Greek-Parliament-Proceedings-1989-2020#-different-topics-across-the-years)

[Pairwise Similarities Between Parliament Members](https://github.com/MariosKadriu/Analysis-of-the-Greek-Parliament-Proceedings-1989-2020#-pairwise-similarities-between-parliament-members)

[Most Important Keywords Across Years](https://github.com/MariosKadriu/Analysis-of-the-Greek-Parliament-Proceedings-1989-2020#-most-important-keywords-across-years)

[Deviation from parliamentary speeches due to economic crisis](https://github.com/MariosKadriu/Analysis-of-the-Greek-Parliament-Proceedings-1989-2020#-deviation-from-parliamentary-speeches-due-to-economic-crisis)

[Clustering of Members of Parliament based on their speeches](https://github.com/MariosKadriu/Analysis-of-the-Greek-Parliament-Proceedings-1989-2020#-clustering-of-members-of-parliament-based-on-their-speeches)

[Gender Relevance Issues](https://github.com/MariosKadriu/Analysis-of-the-Greek-Parliament-Proceedings-1989-2020#-gender-relevance-issues)

## 📝 Description

This project was conducted as part of the Technologies for Big Data Analytics course during my postgraduate studies in the Data and Web Science program at Aristotle University of Thessaloniki. It is an analysis of speeches delivered in the Greek Parliament from 1989 to 2020, using big data analytics methods for knowledge extraction.

My team and I focused on the following tasks:
* Identification of different topics by year
* Identifying similarities between pairs of MPs in terms of their rationale
* Identifying the most important keywords by year
* Identify whether and to what extent a milestone in the political-economic scene, such as the economic crisis, influences the topics of speeches over the years
* Form clusters of parliamentarians based on their reasons for speaking out
* Examine gender issues related to the presence of MPs in parliament

For the processing of the findings, the data was preprocessed in Python using Pandas, while Apache Spark in the Scala language was used for the analysis.

## 📚 Dataset

The dataset includes 1,194,407 speeches by Greek members of parliament, totaling 2.15 GB, exported from 5,118 session files and ranging chronologically from 1989 to 2019.

The dataset was created in the following three basic steps:

* Collection of the records of the parliamentary sessions published on the website of the Greek Parliament https://www.hellenicparliament.gr/Praktika/Synedriaseis-Olomeleias

* Collection of the officially published names of all members of the Greek Parliament from the website of the Greek Parliament https://www.hellenicparliament.gr/Vouleftes/DiatelesantesVouleftes-Apo-Ti-Metapolitefsi-Os-Simera

* Identifying the speakers and their corresponding speeches from the records and matching the names of the speakers indicated in the records with the official names of the members of the parliament

The dataset consists of a .csv file in UTF-8 encoding and contains the following data columns:
* member_name: The official name of the member of parliament who spoke during a session.
* sitting_date: The date on which the session took place 
There are cases where more than one session took place on the same date.
* parliamentary_period: The name and/or number of the parliamentary period in which the speech took place. A parliamentary period includes several parliamentary sessions.
* parliamentary_session: the name and/or number of the parliamentary session in which the speech was made. A parliamentary session includes several parliamentary sessions.
* parliamentary_session: The name and/or number of the parliamentary session in which the speech was made.
* political_party: The political party to which the speaker belongs.
* speaker_info: Information about the speaker taken from the text of the session minutes and related to the speaker's parliamentary role, such as Speaker of Parliament, Minister of Finance, or similar.
* speech: the speech made by the member during the session of the Greek Parliament.
All fields that do not contain information due to omissions in the record files are filled with a NaN value, except for the speech field, which remains an empty string in such cases.

Dataset can be found here: https://github.com/iMEdD-Lab/Greek_Parliament_Proceedings

## 🖥️ Installation

### 🛠️ Requirements
* Apache Spark
* Scala
* Python >= 3.6
* Pandas
* NLTK
* NumPy

## ⏳ Data Preprocessing

For this process we used the libraries Pandas, NLTK and NumPy. The data was processed using Python. The setup used was an Intel i7 10th Gen and 16 GB RAM. The total run time was about 6 hours.

The first step was to remove the speeches of the eight vice presidents of the parliaments, as their content was irrelevant for the purpose of our analysis. The next step was to add spaces after the punctuation marks, as they were missing and caused the problem through the tokenization process. Since the dataset was quite large and contained a lot of information irrelevant to our analysis, we omitted some columns.

The following columns were removed:

* parliamentary_period
* parliamentary_session
* parliamentary_sitting
* member_region

We also deleted all the rows that contained NaN (missing) values in the columns: 

* member_name
* roles
* speech
* sitting_date

Then we prepared the data in the following steps:

* Lowercase everything.
* Remove punctuation marks.
* Remove numbers.
* Keep only the year of the session date.
* Lemmatization.
* Tokenize the speeches.
* Remove stop words from our list.
* Remove all empty tokenized spaces.
* Delete any lines with speeches that were left blank.
* Reassemble the tokens into one speech.
* Remove the extra spaces.


## 🎯 Different Topics Across The Years
The first topic of our analysis was to determine the trending topics per year. The algorithm was configured to provide twenty topics per year. Each topic consists of 10 words.

### Implementation
After preprocessing, all words were converted to vector format using TF-IDF to run LDA and create 20 topics for each year separately. The running time was about 6 minutes per year.

### Results and findings
As an example, we present a list of topics1 over the years along with the political or social events with which they are associated.

From the topics of 1989 we can separate the following issues, which refer to the Koskota scandal and the case "Βρώμικο '89":
* [πασοκ, συνασπισμός, λαός, θέμα, κάθαρση, δημοκρατία, γίνομαι, υπάρχω, ευθύνη, μπορώ]
* [γιαννόπουλος, πασοκ, λύση, οικονομία, *νοέμβρη*, εκλογές, μπορώ, κάθαρση, συνέπεια, τόπος]
* [κοσκωτά, δάσος, πυρκαϊά, εξετάση, δασικός, επιστολή, τράπεζα, συνδικαλισμός, πρόβλημα, σελίδας]

Regarding the previous issues of the 1990s, we find the following, which leads us to the law "For the Protection of Society from Organized Crime" of 1916/1990, which was signed by the organization "17 Νοέμβρη" as a counterweight to the murder of Bakoyannis:
* [ναρκωτικά, τρομοκρατία, αστυνομία, τρομοκράτης, διαμέρισμα, ποινή, κας, νόμος, τύπου, εγκλήμα]

On the occasion of this scandal, we thought of looking for more recent political and social scandals. After some research, we came across the following events and we will see if they show up in the Trending Topics:
* Doping Scandal of 2008: [α, τ, *αθλητισμός*, *ντόπινγκ*, *τροπολογίας*, ασφάλιση, γήπεδο, συμβόλαιας, αρχείας, πηγαίνω]
* Langart List - 2012: [πλειοψηφία, παρακαλώ, λόγο, σδοε, τραπεζικός, *απορρήτος*, κεφαλαιαγορά, *λίστα*, *αγορά*, **ελβετίας**] 
* Langart List - 2013: [**λίστα**, *τράπεζα*, *λαγκάρντ*, λαός, παπακωνσταντίνος, βενιζέλος, εταιρεία, μπορώ, μιλώ, πρωθυπουργός]
* Novartis Scandal - 2019: [novartis, φαρμάκας, πολάκης, τιμή, εοπυυ, υγεία, φαρμακοβιομηχανία, εξηγώ, φαρμακευτικός, δημοκρατία]
* Novartis Scandal - 2020: [λόγο, τράπεζα, novartis, δάνειο, προστατευόμενος, λεπτό, τραπεζής, επιχειρήση, ελευσίνα, μπορώ]
* Siemens Scandal - 2011: [κεφαλογιάννη, πλοίο, τροπολογίας, κρουαζιέρα, νατ, λιμάνι, πηγαίνω, πασοκ, siemens, μπορώ], [εγκληματικότητα, εκατομμύριο, siemens, υπάρχω, πώ, σπίτι, ελληνικός, οπαπ, ληστεία, γίνομαι]
* Siemens Scandal - 2012: 2012: [siemens, εκατομμύριο, περικοπή, αοζ, ναυτιλία, εξεταστικός, βουλή, ελληνικός, δαπανής, άρθρο]

The first topic of 1992 is the following, which refers to the Maastricht Treaty, signed on February 7, 1992:
* [μάαστριχτ, ευρώπη, θέμα, εοκ, υππάρχω, πολιτική, ευρωπαϊκός, εθνικός, κοινότητα, μπορώ]

We went on to 1994 and found a topic about the GATT agreement:
* [δισ, έργο, οικονομία, gatt, πολιτική, κτηνοτρόφος, προϊόντας, γεωργία, αγροτικός, εκατ]

And the first casino licensed by the much discussed law 2206/1994:
* [καζίνο, μπορώ, υπάρχω, θέμα, καζίνος, κράτος, υπουργείο, άρθρο, νομοσχέδιο, εκπαίδευση]

Αs was to be expected that the top topic of 2020 would be the COVID -19 pandemic and related issues:
* [**μέτρο**, *πανδημία*, σχέδιο, *κρίση*, *υγεία*, εργαζόμενος, παραγωγή, προστασία, λαρκο, ενέργεια]
* [**υγεία**, υπάρχω, συριζας, μπορώ, *πανδημία*, δομή, διαδικασία, υπουργείο, αφορώ, ελληνικός]
* [**νοσοκομείο**, αμυντικός, συμφωνία, υπάρχω, *κορωνοϊού*, μπορώ, προσωπικό, δωρεά, περιοχή, **υγεία**]

Finally, we came across some hot topics, such as:
* The name of North Macedonia: 2019
* The Mati fire: 2019
* The refuges flow: 2017, 2018, 2019

Among the results, we can also observe some mixed results, possibly indicating more than one topic. This may be due to the forced number of words for each topic.

## 🎯 Pairwise Similarities Between Parliament Members

### Implementation

After grouping the speeches of each member, all speeches were converted to vector format using the TF-IDF method. Then, the pairwise similarities of the speeches were calculated using cosine similarity. Due to storage space issues, we used a small, hand-selected sample of the dataset. 

The execution time was about two minutes.

### Results and findings
The speeches of the deputies have been grouped so that we can avoid great similarities in speeches that are very small and often meaningless.

![image-1](https://user-images.githubusercontent.com/19438003/197073094-4e982984-22c9-49f3-9f13-42a7597e2eb2.png)

Here we have found some expected results:
* Avlonitou and Dritseli are both Syriza, women and young, so it could be a logical correlation 
* Venizelos and Loverdos both belong to the same party as the bard with Koutsoumba.
* However, we also found some "interesting" results, such as the correlation of Venizelos with Tsipras and Kasidiaris.

## 🎯 Most Important Keywords Across Years

### Implementation

The first step was to group the speeches by the member and also by the session date of the speech. Then, all the words from the speeches were converted into vector form using the method TF-IDF. In the end, the most important keywords per member were found. We performed this particular task for 2015-2020 and the running time was about 4 minutes.

### Results and findings

In the following table you will find some preliminary results:

* Mr. Vavitsiotis dealt with Brexit in 2020, while he dealt with Anel in 2016, which is confirmed by the government reshuffle at that time and Barvitsiotis' position on the issue.

<p align="center">
  <img src="https://user-images.githubusercontent.com/19438003/197073958-2ef15daa-1bd2-4f00-af89-6b133d7723ac.png" />
</p>

## 🎯 Deviation from parliamentary speeches due to economic crisis

### Implementation

Originally, 2 data sets were created. The first data set covered the years 2004 to 2008 before the economic crisis. The second data set covered the years 2008 to 2013 after the economic crisis. For both data sets, an 
LDA algorithm was run to see what topics were discussed before and after the financial crisis. The execution time was approximately 3 minutes.

### Results and findings

At first glance, there is no obvious divergence between the two eras. Then we decided to be more creative and find some criteria for virgins that we can verify 

We proceeded to the following analysis:

* We counted the number of the word "κριση" 
(crisis) itself. The word appeared eight times more often after the 2008 benchmark.

* We counted the words that had an economic theme, such as "οικονομία, δανεισμός, μνημόνιο, κρίση". Words related to the economy appeared ~18% more.

* New words like "Μνημόνιο".

## 🎯 Clustering of Members of Parliament based on their speeches

### Implementation

After grouping words from all speech data by session date for a decade from 2009 to 2019, the words were converted to Word2Vec vectors 
The data were then divided into training and test groups to train the KMeans algorithm, which clusters the members and parties in each group 
Finally, the total number of speeches in each cluster was determined. We used 3 clusters after manually testing the use of seven and five clusters. The running time was approximately 4 minutes.

### Results and findings

Τhe distribution of members among three clusters was relatively even in contrast to the use of five or seven clusters. The table below shows the distribution of future members of Parliament.

<p align="center">
  <img src="https://user-images.githubusercontent.com/19438003/197074690-35d99bdb-b08f-4ed1-83a3-fcfe57144163.png" />
</p>

In order to check whether the separation has a reasonable explanation, we analyzed the individual clusters 
In the analysis, we arrived at the following results.

* Most of the KKE members were assigned to the "1 cluster"
* Most members of LAOS were assigned to the "2 cluster"
* Most members of the People's Association - Golden Dawn were assigned to the "2" cluster
* Larger parties have shown a greater deviation.

From the above data we can abstractly assume that 
Cluster 1 contains more left-leaning members in contrast to Cluster 2, which seems to gather more right-leaning members.
Although the larger parties showed a larger deviation, the clustering also followed a pattern where more right-leaning parties such as the 
Nea Dimokratia had more members in cluster 2. The deviation can also be explained by the fact that it is common in the larger parties for a member to move from one party to another over the years.

## 🎯 Gender Relevance Issues
Ιn this task we chose to analyze issues related to gender discrimination. First, we examined the presence of women over the years and then outlined the main themes mentioned in the speeches of women and men, respectively, in order to identify semantic differences. The running time was approximately 8 minutes.

### Implementation
For each year, the total number of speeches and their length were calculated by sitting date and member gender. In addition, the total number of women and men per year is calculated. Finally, after converting all the words from the speeches of women and men separately into TF-IDF vectors, we perform LDA to obtain the different topics.

### Results and findings

<p align="center">
  <img src="https://user-images.githubusercontent.com/19438003/197075854-e9ec788a-0157-4a77-a6b9-574ea73683bd.png" />
</p>

As we can see in the graph. The appearance of women generally has an upward trend, as does the percentage of speeches given by women and the length of the speech itself.

Percentage of speeches between men and women            |  Percentage of the length of speeches between men and women
:-------------------------:|:-------------------------:
![image-5](https://user-images.githubusercontent.com/19438003/197076713-a388d948-6da9-4234-b3f1-944262353143.png)  |  ![image-6](https://user-images.githubusercontent.com/19438003/197076869-0a7209f0-e7e7-457a-b652-b3123e6136dc.png)

From the topic analysis for each gender:
* Unique topics for men -> δημοκρατία, δικαιοσύνη, αγροτικός, εργαζόμενος, ναρκωτικά, διάταξη
* Unique topics for women -> οικονομία, υγεία, δημόσιος, εργασία, βία, περιβαλλοντικός, αγώνας 
* Topic discussed more by men than by women -> νομοσχέδιο
* Topic that was discussed more by women than by men -> έργο
* Topic that was discussed by men and women equally -> ανάπτυξη, πολιτική
