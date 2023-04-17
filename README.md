# HackNite
<h2><u>Team ScrapBin</u>
<br><br>
Track: Automation</h2>
<h3>Project Overview:</h3>
<p>The project allows the users to look up the medicine or the treatment required and compares the prices, offers and delivery time of the entered medication offered by different pharmaceutical stores.</p>
<h3>Projet Description:</h3>
<ul>
  <li>
<p>The project has an Android App as its front-end interface where the user first creates an account. This is handled by <code>Google's Firebase API</code> whose's Authentication service is being used for maintaining the accounts. After the sign-up/sign-in is successful, the user is taken to the page which has an <code>EditText</code> element. It takes the medicine or the treatment as the input. It then scrapes the <a href="1mg.com">1mg</a> website and gets a list of related items and displays it in a <code>ListView</code> element.</p>
<br>
<p>The <code>ListView</code> element has an <code>onClick</code> event which then takes the user to a new activity. This activity starts with an <code>AlertDialog</code> pop-up which asks the user to enter the pin-code of the destination address. It then [scrapes](#scraping-the-website) the website with the name of the medication and gets the Image, Cost, Offers and the [Ordering time](#ordering-time). This data is displayed in <code>CardView</code> elements which are contained in a <code>RecyclerView</code> element</p>
  </li>
  <li>
  </li>
</ul>
