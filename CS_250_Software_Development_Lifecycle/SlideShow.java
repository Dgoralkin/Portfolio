import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SlideShow extends JFrame {

    // Declare Variables
    private JPanel slidePane;
    private JPanel textPane;
    private JPanel buttonPane;
    private JPanel bottomPane; // Combined panel for textPane and buttonPane
    private CardLayout card;
    private CardLayout cardText;
    private JButton btnPrev;
    private JButton btnNext;
    private JLabel lblSlide;
    private JLabel lblTextArea;
    private JButton btnOrderNow;

    /**
     * Create the application.
     */
    public SlideShow() throws HeadlessException {
        initComponent();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initComponent() {
        // Initialize variables to empty objects
        card = new CardLayout();
        cardText = new CardLayout();
        slidePane = new JPanel();
        textPane = new JPanel();
        textPane.setBackground(new Color(128, 255, 255));
        buttonPane = new JPanel();
        bottomPane = new JPanel(); // Initialize the new combined panel
        btnPrev = new JButton();
        btnNext = new JButton();
        lblSlide = new JLabel();
        lblTextArea = new JLabel();

        // Add Company logo
        setIconImage(Toolkit.getDefaultToolkit().getImage(SlideShow.class.getResource("resources/SNHU.png")));

        // Setup frame attributes
        setSize(800, 600);
        setLocationRelativeTo(null);
        setTitle("SNHU Travel - Top 5 Detox/Wellness Travel Packages.");
        getContentPane().setLayout(new BorderLayout(10, 50));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setting the layouts for the panels
        slidePane.setLayout(card);
        textPane.setLayout(cardText);

        // Logic to add each of the slides and text
        for (int i = 1; i <= 5; i++) {
            lblTextArea = new JLabel();
            lblSlide = new JLabel();
            lblSlide.setText(getResizeIcon(i));
            lblTextArea.setText(getTextDescription(i));
            slidePane.add(lblSlide, "card" + i);
            textPane.add(lblTextArea, "cardText" + i);
        }

        getContentPane().add(slidePane, BorderLayout.CENTER);

        // Wrap textPane with a JScrollPane to make it slidable
        JScrollPane scrollableTextPane = new JScrollPane(textPane);
        scrollableTextPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableTextPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Set buttons at the center of the frame
        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Set "Previous" button
        btnPrev.setText("Previous");
        btnPrev.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                goPrevious();
            }
        });
        buttonPane.add(btnPrev);

        // Set "Next" button
        btnNext.setText("Next");
        btnNext.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                goNext();
            }
        });

        // Order Now Button Functionality
        btnOrderNow = new JButton("Order Now");
        btnOrderNow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Order Placed - Thank you!!");
            }
        });
        btnOrderNow.setBackground(new Color(240, 240, 240));
        btnOrderNow.setFont(new Font("Tahoma", Font.BOLD, 10));
        buttonPane.add(btnOrderNow);
        buttonPane.add(btnNext);

        // Combine scrollableTextPane and buttonPane into bottomPane
        bottomPane.setLayout(new BoxLayout(bottomPane, BoxLayout.Y_AXIS));
        bottomPane.add(scrollableTextPane);
        bottomPane.add(buttonPane);

        // Center the buttonPane horizontally
        buttonPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        getContentPane().add(bottomPane, BorderLayout.SOUTH);
    }

    /**
     * Previous Button Functionality
     */
    private void goPrevious() {
        card.previous(slidePane);
        cardText.previous(textPane);
    }

    /**
     * Next Button Functionality
     */
    private void goNext() {
        card.next(slidePane);
        cardText.next(textPane);
    }

    /**
     * Method to get the images
     */
    private String getResizeIcon(int i) {
        String image = "";
        // Display Package image #1
        if (i == 1) {
            image = "<html><body><img width= '800' height='500' src='" + getClass().getResource("/resources/Detox_wellness_retreat_1.jpg") + "'</body></html>";
        } else if (i == 2) {
            image = "<html><body><img width= '800' height='500' src='" + getClass().getResource("/resources/Detox_wellness_retreat_2.jpg") + "'</body></html>";
        } else if (i == 3) {
            image = "<html><body><img width= '800' height='500' src='" + getClass().getResource("/resources/Detox_wellness_retreat_3.jpg") + "'</body></html>";
        } else if (i == 4) {
            image = "<html><body><img width= '800' height='500' src='" + getClass().getResource("/resources/Detox_wellness_retreat_4.jpg") + "'</body></html>";
        } else if (i == 5) {
            image = "<html><body><img width= '800' height='500' src='" + getClass().getResource("/resources/Detox_wellness_retreat_5.jpg") + "'</body></html>";
        }
        return image;
    }

    /**
     * Method to get the text values
     */
    private String getTextDescription(int i) {
        String text = "";
        if (i == 1) {
            text = "<html><body><font size='5'>#1 - Vital Body Cleanse at The BARAI Spa, Thailand.</font>"
                    + "<br>For a healthy transformation in paradise, escape on this intensive detox retreat in tropical Thailand.</body></html>";
        } else if (i == 2) {
            text = "<html><body><font size='5'>#2 - Master Detox at Sianji Well-Being Resort, Turkey.</font>"
                    + "<br>For an affordable healthy holiday which promises results, escape to this specialist intensive detox retreat in a secluded coastal area.</body></html>";
        } else if (i == 3) {
            text = "<html><body><font size='5'>#3 - Detox at Lefay Resort & SPA Lago di Garda, Italy.</font>"
                    + " <br>Transform your body on a wellness holiday in Lake Garda with healthy nutrition.</body></html>";
        } else if (i == 4) {
            text = "<html><body><font size='5'>#4 - Detox at Ananda in the Himalayas, India.</font>"
                    + " <br>Located in the breath-taking Himalayas, this award-winning detox holiday in India.</body></html>";
        } else if (i == 5) {
            text = "<html><body><font size='5'>#5 - Wellbeing Detox at Euphoria Retreat, Greece.</font>"
                    + " <br>Dive into your wellbeing aspirations by detoxing and rebalancing your mind, body and soul for the ultimate wellness holiday.</body></html>";
        }
        return text;
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                SlideShow ss = new SlideShow();
                ss.setVisible(true);
            }
        });
    }
}
