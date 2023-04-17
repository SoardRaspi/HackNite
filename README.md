# HackNite
<h2><u>Team ScrapBin</u>
<br><br>
Track: Automation</h2>
<h3>Project Overview:</h3>
<p>The project allows the users to look up the medicine or the treatment required and compares the prices, offers and delivery time of the entered medication offered by different pharmaceutical stores.</p>
<h3>Projet Description:</h3>
<ul>
  <li>
    <p>The project has an Android App made in <code>Java</code> as its front-end interface where the user first creates an account. This is handled by <code>Google's Firebase API</code> whose's Authentication service is being used for maintaining the accounts. After the sign-up/sign-in is successful, the user is taken to the page which has an <code>EditText</code> element. It takes the medicine or the treatment as the input. It then scrapes the <a href="1mg.com">1mg</a> website and gets a list of related items and displays it in a <code>ListView</code> element.</p>
    <br>
    <p>The <code>ListView</code> element has an <code>onClick</code> event which then takes the user to a new activity. This activity starts with an <code>AlertDialog</code> pop-up which asks the user to enter the pin-code of the destination address. It then [scrapes](#for-the) the website with the name of the medication and gets the Image, Cost, Offers and the [Delivery time] [b]. This data is displayed in <code>CardView</code> elements which are contained in a <code>RecyclerView</code> element</p>
  </li>
  <li>
    <p>The scraping is done on the <code>Flask Server</code> which runs on the backend. The server is written in <code>Python</code>. To make it accessible to other devices connected on the same network, <code>ngrok</code> is used to create a channel which makes the localhost public on the network. The scraping operation is done twice by the app. For the purpose of creating a prototype, the server runs locally and hence the link provided by ngrok needs to be updated in the <code><a href="https://github.com/SoardRaspi/HackNite/blob/main/Android%20App/app/src/main/java/com/example/hacknite/Constants.java">Constant.java</a></code> file and reuploaded. This can be automated by running the server on sites like Heroku or Pythonanywhere.</p>
    <br>
    <p>In the app, scraping the website is done in two activities. The first time, it is done in <code><a href="https://github.com/SoardRaspi/HackNite/blob/main/Android%20App/app/src/main/java/com/example/hacknite/SelectionAndHistory.java">SelectionAndHistory.java</a></code> file to get the list of all the related medications. When the list item is clicked, it navigates to the <code>ComparisonActivity</code> activity with the name of the medication.</p>
    <br>
    <p>The second time, it is done in the <code><a href="https://github.com/SoardRaspi/HackNite/blob/main/Android%20App/app/src/main/java/com/example/hacknite/ComparisonActivity.java">ComparisonActivity.java</a></code> file. In this activty, it scrapes the website for the selected medication and scrapes #<a name="for-the"></a> for the:</p>
    <ul>
      <li>the image's URL (by searching for the class name).</li>
      <li>the cost (by searching for the class name).</li>
      <li>the related offers (by searching for the class name).</li>
      <li>the [b]: "Delivery time" delivery time (by doing a small operation where the pincode entered by the user is used.)</li>
    </ul>
  </li>
</ul>
