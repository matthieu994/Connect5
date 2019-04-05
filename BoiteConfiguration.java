/*
 * INF4230 - Intelligence artificielle
 * UQAM - Département d'informatique
 */

package Connect5Game;

import javax.swing.JFileChooser;

public class BoiteConfiguration extends javax.swing.JDialog {

    JoueurConfig jc1, jc2;

    /** Creates new form PlaneteHConfig */
    public BoiteConfiguration(java.awt.Frame parent, boolean modal) {

        super(parent, modal);
        initComponents();

        jc1 = new JoueurConfig();
        jc2 = new JoueurConfig();

        jc1.setBorder(javax.swing.BorderFactory.createTitledBorder("Joueur R"));
        jc2.setBorder(javax.swing.BorderFactory.createTitledBorder("Joueur V"));

        jPanel1.add(jc1);
        jPanel1.add(jc2);
        pack();
    }

    public Joueur[] getJoueurs(){
        return new Joueur[] {jc1.getJoueur(), jc2.getJoueur() };
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jPanel1 = new javax.swing.JPanel();
    jPanel4 = new javax.swing.JPanel();
    jPanel2 = new javax.swing.JPanel();
    jLabel3 = new javax.swing.JLabel();
    nbLignesTF = new javax.swing.JTextField();
    jLabel4 = new javax.swing.JLabel();
    nbColsTF = new javax.swing.JTextField();
    jPanel3 = new javax.swing.JPanel();
    jLabel5 = new javax.swing.JLabel();
    delaisReflexion = new javax.swing.JTextField();
    ignorerRetardCB = new javax.swing.JCheckBox();
    jLabel6 = new javax.swing.JLabel();
    tolerate50pCB = new javax.swing.JCheckBox();
    jPanel5 = new javax.swing.JPanel();
    jButton1 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Configuration de Connect 5");

    jPanel1.setPreferredSize(new java.awt.Dimension(700, 110));
    jPanel1.setLayout(new java.awt.GridLayout(1, 0));
    getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

    jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder());
    jPanel4.setPreferredSize(new java.awt.Dimension(0, 100));
    jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.X_AXIS));

    jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Grille"));
    jPanel2.setLayout(new java.awt.GridBagLayout());

    jLabel3.setText("Nombre de lignes :");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(20, 10, 0, 0);
    jPanel2.add(jLabel3, gridBagConstraints);

    nbLignesTF.setText("12");
    nbLignesTF.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        nbLignesTFActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.ipadx = 34;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(20, 3, 0, 6);
    jPanel2.add(nbLignesTF, gridBagConstraints);

    jLabel4.setText("Nombre de colonnes : ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
    jPanel2.add(jLabel4, gridBagConstraints);

    nbColsTF.setText("N/A");
    nbColsTF.setEnabled(false);
    nbColsTF.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        nbColsTFActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.ipadx = 34;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(10, 3, 30, 6);
    jPanel2.add(nbColsTF, gridBagConstraints);

    jPanel4.add(jPanel2);

    jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));
    jPanel3.setLayout(new java.awt.GridBagLayout());

    jLabel5.setText("Temps de réflexion : ");
    jPanel3.add(jLabel5, new java.awt.GridBagConstraints());

    delaisReflexion.setText("2000");
    delaisReflexion.setMinimumSize(new java.awt.Dimension(100, 20));
    delaisReflexion.setPreferredSize(new java.awt.Dimension(60, 20));
    delaisReflexion.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        delaisReflexionActionPerformed(evt);
      }
    });
    jPanel3.add(delaisReflexion, new java.awt.GridBagConstraints());

    ignorerRetardCB.setText("Ignorer les dépassements de temps (utile pour développement)");
    ignorerRetardCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        ignorerRetardCBActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    jPanel3.add(ignorerRetardCB, gridBagConstraints);

    jLabel6.setText(" millisecondes (ms)");
    jPanel3.add(jLabel6, new java.awt.GridBagConstraints());

    tolerate50pCB.setSelected(true);
    tolerate50pCB.setText("Tolérer un retard de 50% du temps et pénaliser le coup suivant");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    jPanel3.add(tolerate50pCB, gridBagConstraints);

    jPanel4.add(jPanel3);

    jPanel5.setPreferredSize(new java.awt.Dimension(100, 10));
    jPanel5.setLayout(new java.awt.GridBagLayout());

    jButton1.setText("Fermer");
    jButton1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    jButton1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });
    jPanel5.add(jButton1, new java.awt.GridBagConstraints());

    jPanel4.add(jPanel5);

    getContentPane().add(jPanel4, java.awt.BorderLayout.SOUTH);

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void nbLignesTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nbLignesTFActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_nbLignesTFActionPerformed

    private void nbColsTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nbColsTFActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_nbColsTFActionPerformed

    private void delaisReflexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delaisReflexionActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_delaisReflexionActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void ignorerRetardCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ignorerRetardCBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ignorerRetardCBActionPerformed
    
    JFileChooser filechooser = new JFileChooser();
    
  // Variables declaration - do not modify//GEN-BEGIN:variables
  protected javax.swing.JTextField delaisReflexion;
  public javax.swing.JCheckBox ignorerRetardCB;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JPanel jPanel5;
  protected javax.swing.JTextField nbColsTF;
  protected javax.swing.JTextField nbLignesTF;
  public javax.swing.JCheckBox tolerate50pCB;
  // End of variables declaration//GEN-END:variables
    
}
